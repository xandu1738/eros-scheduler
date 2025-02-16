package com.ceres.erostabler.models;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.ceres.erostabler.dto.Developer;
import com.ceres.erostabler.dto.WeekDay;
import com.ceres.erostabler.services.InitializationService;
import com.ceres.erostabler.services.QuotesService;
import com.ceres.erostabler.utils.MailService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class SchedulingService {

    private final InitializationService initializationService;
    private final MailService mailService;
    private final QuotesService quotesService;
    private Model model;


    public String scheduleDaysOff(Model model) {
        // Devs list
        List<Developer> devs = initializationService.getDevs();

        Collections.shuffle(devs);
        Collections.shuffle(devs);

        List<Developer> developers = getJuniorDevelopers(devs);

        Map<String, List<Developer>> waList = assignDevelopers(developers);

        JSONObject randomQuote = resolveQuote();

        model.addAttribute("schedule", waList);
        model.addAttribute("quote", randomQuote.getString("quote"));
        model.addAttribute("author", randomQuote.getString("author"));
        return "schedule";
    }

    private JSONObject resolveQuote() {
        String quote = "Challenges are what make life interesting and overcoming them is what makes life meaningful";
        String author = "Joshua J. Marine";

        var randomQuoteList = quotesService.getRandomQuote();
        var randomQuote = new JSONObject();

        if (!randomQuoteList.isEmpty()) {
            randomQuote = JSON.parseObject(JSON.toJSONString(randomQuoteList.get(0)));
        }

        if (!randomQuote.isEmpty()) {
            quote = randomQuote.getString("q");
            author = randomQuote.getString("a");
        }
        JSONObject quoteObject = new JSONObject();
        quoteObject.put("quote", quote);
        quoteObject.put("author", author);

        return quoteObject;
    }

    private Map<String, List<Developer>> assignDevelopers(List<Developer> developers) {
        // Days of the week
        List<WeekDay> weekDays = new ArrayList<>(new HashSet<>(initializationService.getWeekDays()));

        // in case developers list is empty
        if (developers.isEmpty()) {
            throw new IllegalStateException("Please provide list of staff.");
        }

        // Map to hold final assignments
        Map<WeekDay, List<Developer>> weekdayAssignments = weekDays.stream()
                .collect(Collectors.toMap(day -> day, day -> new ArrayList<>()));

        // Map to track developer counts
        Map<Developer, Integer> developerAssignments = developers.stream()
                .collect(Collectors.toMap(dev -> dev, dev -> 0, (existing, replacement) -> existing));

        // Assign each developer at least once
        enforceMinimumAssignments(developers, weekDays, weekdayAssignments, developerAssignments);

        // Assign remaining slots while enforcing constraints
        assignRemainingSlots(developers, weekDays, weekdayAssignments, developerAssignments);

        // Sort by weekday ID
        // Use weekday name as the key
        // Flatten and remove duplicates
        // Merge function (not needed but required by toMap)
        // Use LinkedHashMap to maintain insertion order
        return weekdayAssignments.entrySet().stream()
                .sorted(Comparator.comparing(entry -> entry.getKey().id())) // Sort by weekday ID
                .collect(Collectors.toMap(
                        entry -> entry.getKey().name(), // Use weekday name as the key
                        entry -> entry.getValue().stream().distinct().toList(), // Flatten and remove duplicates
                        (existing, replacement) -> existing, // Merge function (not needed but required by toMap)
                        LinkedHashMap::new // Use LinkedHashMap to maintain insertion order
                ));
    }

    private static List<Developer> getJuniorDevelopers(List<Developer> devs) {
        // Devs Minus Supes
        List<Developer> jrs = devs.stream()
                .filter(d -> !d.name().contains("Mart"))
                .filter(d -> !d.name().contains("John Mark"))
                .filter(d -> !d.name().contains("Gorret"))
                .toList();

        // Minus any repetitions
        return new ArrayList<>(new HashSet<>(jrs));
    }

    @Scheduled(cron = "0 0 15 * * 7")
    public void makeSchedule() {
        List<Developer> devs = initializationService.getDevs();
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        Map<String, List<Developer>> waList = assignDevelopers(getJuniorDevelopers(devs));

        sendMailToDevs(devs, waList,executor);
    }

    private void enforceMinimumAssignments(
            List<Developer> developers,
            List<WeekDay> weekdays,
            Map<WeekDay, List<Developer>> weekdayAssignments,
            Map<Developer, Integer> developerAssignments) {
        Collections.shuffle(developers);

        int currentDayIndex = 0;

        log.info("[+] Enforcing minimum assignments...");
        for (Developer dev : developers) {
            WeekDay day = weekdays.get(currentDayIndex % weekdays.size());
            log.warn("[+] Adding {} to {}...", dev.name(), day.name());
            weekdayAssignments.get(day).add(dev);
            developerAssignments.put(dev, developerAssignments.get(dev) + 1);
            currentDayIndex++;
        }
    }

    private void assignRemainingSlots(
            List<Developer> developers,
            List<WeekDay> weekdays,
            Map<WeekDay, List<Developer>> weekdayAssignments,
            Map<Developer, Integer> developerAssignments) {
        Collections.shuffle(developers);

        final int MAX_DEVS_PER_DAY = 2;
        final int MAX_DAYS_PER_DEV = 1;

        ThreadLocalRandom random = ThreadLocalRandom.current();

        for (WeekDay day : weekdays) {
            log.info("****** {} ******", day.name());
            int maxDevsForDay = (day.name().equalsIgnoreCase("Monday") || day.name().equalsIgnoreCase("Tuesday"))
                    ? 1
                    : MAX_DEVS_PER_DAY;
            List<Developer> assignedDevs = weekdayAssignments.get(day);

            log.info("[+] Max devs {}", maxDevsForDay);

            // Attempts to limit number of attempts to execute canAssign to avoid infinite loop
            int attempts = 0;

            // Continue until the devs per day condition is satisfied
            while (assignedDevs.size() <= maxDevsForDay && attempts < developers.size() * 2) {
                Developer randomDev = developers.get(random.nextInt(developers.size()));
                log.info("[+] Assigning {}\n\n", randomDev.name());

                if (canAssign(randomDev, day, weekdayAssignments, developerAssignments, weekdays, maxDevsForDay, MAX_DAYS_PER_DEV)) {
                    assignedDevs.add(randomDev);
                    // Get or default handles unassigned devs gracefully
                    developerAssignments.put(randomDev, developerAssignments.getOrDefault(randomDev, 0) + 1);
                }
                attempts++;
            }
        }
    }

    private boolean canAssign(
            Developer dev,
            WeekDay currentDay,
            Map<WeekDay, List<Developer>> weekdayAssignments,
            Map<Developer, Integer> developerAssignments,
            List<WeekDay> weekdays,
            int maxDevsPerDay,
            int maxDaysPerDev) {

        log.info("[+] Checking if {} is assigned", dev.name());
        // Check if developer already reached their max allowed days
        Integer devAssignments = developerAssignments.getOrDefault(dev, 0);
        if (devAssignments >= maxDaysPerDev) {
            return false;
        }

        // Check if the current day already has max developers assigned
        log.info("[+] Checking if weekday {} has max developers...", currentDay.name());
        List<Developer> currentDayAssignments = weekdayAssignments.getOrDefault(currentDay, new ArrayList<>());
        if (currentDayAssignments.size() >= maxDevsPerDay) {
            log.info("[+] Yes it does ...");
            return false;
        }

        log.info("[+] Checking if {} has been assigned to the previous day...", dev.name());
        // Check if the developer is already assigned to the previous day
        int currentIndex = weekdays.indexOf(currentDay);
        if (currentIndex > 0) {
            WeekDay previousDay = weekdays.get(currentIndex - 1);
            List<Developer> previousDayAssignments = weekdayAssignments.getOrDefault(previousDay, new ArrayList<>());

            // Ensure developer is not assigned to both previous and current day
            log.info("[+] Status {}", String.valueOf(!previousDayAssignments.contains(dev) && !currentDayAssignments.contains(dev)).toUpperCase());
            return !previousDayAssignments.contains(dev) && !currentDayAssignments.contains(dev);
        }

        // If all checks pass, assignment is allowed
        return true;
    }


    private void sendMailToDevs(List<Developer> developers, Map<String, List<Developer>> weekdayAssignments, ExecutorService executor) {
//        CountDownLatch latch = new CountDownLatch(developers.size());
        try {
            developers.forEach(
                    dev -> {
                        executor.submit(
                                () -> {
                                    try {
                                        mailService.sendTemplateMail(
                                                dev,
                                                "Weeks Schedule",
                                                weekdayAssignments,
                                                resolveQuote(),
                                                "email-template"
                                        );
                                    } catch (MessagingException | UnsupportedEncodingException e) {
                                        throw new RuntimeException(e.getMessage(), e);
                                    }
                                }
                        );

                    }
            );
        } finally {
            log.info("[+] Initiating shut down...");
            shutdownExecutor(executor);
        }
    }

    private static void shutdownExecutor(ExecutorService executor) {
        executor.shutdown();
        try {
            // Wait for all tasks to complete or timeout after 1 minute
            if (!executor.awaitTermination(1, TimeUnit.MINUTES)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

}
