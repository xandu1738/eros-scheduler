package com.ceres.erostabler.models;

import com.ceres.erostabler.dto.Developer;
import com.ceres.erostabler.dto.WeekDay;
import com.ceres.erostabler.services.InitializationService;
import com.ceres.erostabler.utils.MailService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SchedulingService {

    private final InitializationService initializationService;
    private final MailService mailService;

    public String scheduleDaysOff(Model model) {
        // Devs list
        List<Developer> devs = initializationService.getDevs();

        // Devs Minus Mart
        List<Developer> jrs = devs.stream()
                .filter(d -> !d.name().contains("Mart"))
                .toList();

        // Minus any repetitions
        List<Developer> developers = new ArrayList<>(new HashSet<>(jrs));

        // Days of the week
//        List<String> weekdays = List.of("Monday", "Tuesday", "Wednesday", "Thursday", "Friday");
        List<WeekDay> weekDays = new ArrayList<>(new HashSet<>(initializationService.getWeekDays()));

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

        Map<String, List<Developer>> waList = weekdayAssignments.entrySet().stream()
                .sorted(Comparator.comparing(entry -> entry.getKey().id())) // Sort by weekday ID
                .collect(Collectors.toMap(
                        entry -> entry.getKey().name(), // Use weekday name as the key
                        entry -> entry.getValue().stream().distinct().toList(), // Flatten and remove duplicates
                        (existing, replacement) -> existing, // Merge function (not needed but required by toMap)
                        LinkedHashMap::new // Use LinkedHashMap to maintain insertion order
                ));

        model.addAttribute("schedule", waList);

        sendMailToDevs(devs, waList);

        return "schedule";
    }

    private void enforceMinimumAssignments(
            List<Developer> developers,
            List<WeekDay> weekdays,
            Map<WeekDay, List<Developer>> weekdayAssignments,
            Map<Developer, Integer> developerAssignments) {

        int currentDayIndex = 0;

        for (Developer dev : developers) {
            WeekDay day = weekdays.get(currentDayIndex % weekdays.size());
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

        final int MAX_DEVS_PER_DAY = 2;
        final int MAX_DAYS_PER_DEV = 2;

        Random random = new Random();

        for (WeekDay day : weekdays) {
            List<Developer> assignedDevs = weekdayAssignments.get(day);

            // Continue until the devs per day condition is satisfied
            while (assignedDevs.size() < MAX_DEVS_PER_DAY) {
                Developer randomDev = developers.get(random.nextInt(developers.size()));

                if (canAssign(randomDev, day, weekdayAssignments, developerAssignments, weekdays, MAX_DEVS_PER_DAY, MAX_DAYS_PER_DEV)) {
                    assignedDevs.add(randomDev);
                    developerAssignments.put(randomDev, developerAssignments.get(randomDev) + 1);
                }
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

        // Check if developer already reached max days
        if (developerAssignments.get(dev) >= maxDaysPerDev) return false;

        // Check if the developer is already assigned to the previous day
        int currentIndex = weekdays.indexOf(currentDay);
        if (currentIndex > 0) {
            var previousDay = weekdays.get(currentIndex - 1);
            if (weekdayAssignments.get(previousDay).contains(dev) || weekdayAssignments.get(currentDay).contains(dev))
                return false;
        }

        return true;
    }


    private void sendMailToDevs(List<Developer> developers, Map<String, List<Developer>> weekdayAssignments) {
        developers.forEach(
                dev -> {
                    try {
                        mailService.sendTemplateMail(
                                dev,
                                "Weeks Schedule",
                                weekdayAssignments,
                                "email-template"
                        );
                    } catch (MessagingException | UnsupportedEncodingException e) {
                        throw new IllegalStateException(e.getMessage(), e);
                    }
                }
        );
    }

}
