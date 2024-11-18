package com.ceres.erostabler;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SchedulingService {

    private final DeveloperService developerService;

    public SchedulingService(DeveloperService developerService) {
        this.developerService = developerService;
    }

//    public ResponseEntity<Map<String, List<Developer>>> scheduleOffHours() {
//        List<Developer> developers = developerService.getDevs();
//
//        // Days of the week
//        List<String> weekdays = List.of("Monday", "Tuesday", "Wednesday", "Thursday", "Friday");
//
//        // Map to track assignments for each developer to ensure diversity
//        Map<String, Map<Developer, Integer>> dayDeveloperCounts = weekdays.stream()
//                .collect(Collectors.toMap(day -> day, day -> new HashMap<>()));
//
//        // Map to hold the final assignments
//        Map<String, List<Developer>> weekdayAssignments = weekdays.stream()
//                .collect(Collectors.toMap(day -> day, day -> new ArrayList<>()));
//
//        Random random = new Random();
//
//        // Helper to shuffle the developers list
//        Supplier<List<Developer>> shuffledDevelopers = () -> {
//            List<Developer> shuffled = new ArrayList<>(developers);
//            Collections.shuffle(shuffled, random);
//            return shuffled;
//        };
//
/// /        developers.forEach(dev -> {
/// /
/// /        });
//
//        for (int i = 0; i < 2; i++) { // Each developer should be assigned to 2 days
//            // Shuffle the developers list at the start of each iteration
//            List<Developer> reshuffledDevs = new ArrayList<>(developers);
//            Collections.shuffle(reshuffledDevs);
//
//            for (Developer dev : reshuffledDevs) {
//                // Try to assign the developer to a day
//                for (String day : weekdays) {
//                    List<Developer> assignedDevs = weekdayAssignments.get(day);
//                    Map<Developer, Integer> counts = dayDeveloperCounts.get(day);
//
//                    // Check constraints: no more than 3 developers per day and avoid repeating the same day
//                    if (assignedDevs.size() < 3 && counts.getOrDefault(dev, 0) < 1) {
//                        assignedDevs.add(dev);
//                        counts.put(dev, counts.getOrDefault(dev, 0) + 1);
//                        break; // Move to the next developer once assigned
//                    }
//                }
//            }
//        }
//
//
//        // Return the assignment as a JSON response
//        return ResponseEntity.ok(weekdayAssignments);
//    }

//public String scheduleDaysOff(Model model) {
//    List<Developer> developers = developerService.getDevs();
//
//    // Days of the week
//    List<String> weekdays = List.of("Monday", "Tuesday", "Wednesday", "Thursday", "Friday");
//
//    // Map to hold final assignments
//    Map<String, List<Developer>> weekdayAssignments = weekdays.stream()
//            .collect(Collectors.toMap(day -> day, day -> new ArrayList<>()));
//
//    // Map to track developer counts
//    Map<Developer, Integer> developerAssignments = developers.stream()
//            .collect(Collectors.toMap(dev -> dev, dev -> 0));
//
//    Random random = new Random();
//
//    // Shuffle developers for randomness
//    List<Developer> shuffledDevelopers = new ArrayList<>(developers);
//    Collections.shuffle(shuffledDevelopers, random);
//
//    // Assign developers to days
//    for (String day : weekdays) {
//        for (Developer dev : shuffledDevelopers) {
//            List<Developer> assignedDevs = weekdayAssignments.get(day);
//
//            // Check if developer can be assigned (max 3 per day, not 2 consecutive days, max 2 days total)
//            if (assignedDevs.size() < 3 &&
//                    developerAssignments.get(dev) < 2 &&
//                    !isAssignedToPreviousDay(dev, day, weekdays, weekdayAssignments)) {
//                assignedDevs.add(dev);
//                developerAssignments.put(dev, developerAssignments.get(dev) + 1);
//
//                // Stop assigning to this day if it has 3 developers
//                if (assignedDevs.size() == 3) break;
//            }
//        }
//
//        // If a day ends with no assignments (edge case), force assign random developers
//        while (weekdayAssignments.get(day).isEmpty()) {
//            Developer randomDev = shuffledDevelopers.get(random.nextInt(shuffledDevelopers.size()));
//            List<Developer> assignedDevs = weekdayAssignments.get(day);
//
//            if (developerAssignments.get(randomDev) < 2) {
//                assignedDevs.add(randomDev);
//                developerAssignments.put(randomDev, developerAssignments.get(randomDev) + 1);
//            }
//        }
//    }
//
//    model.addAttribute("schedule",weekdayAssignments);
//
//    return "schedule";
//}

public String scheduleDaysOff(Model model) {
    List<Developer> developers = developerService.getDevs();

    List<String> weekdays = List.of("Monday", "Tuesday", "Wednesday", "Thursday", "Friday");

    // Initialize the assignments
    Map<String, List<Developer>> schedule = new LinkedHashMap<>();
    weekdays.forEach(day -> schedule.put(day, new ArrayList<>()));

    Random random = new Random();

    // Track the number of assignments per developer
    Map<Developer, Integer> developerAssignments = new HashMap<>();
    developers.forEach(dev -> developerAssignments.put(dev, 0));

    // Assign developers to each day while ensuring constraints
    for (String day : weekdays) {
        Set<Developer> assigned = new HashSet<>();

        while (assigned.size() < 3) {
            // Shuffle developers and pick one that satisfies constraints
            List<Developer> shuffledDevelopers = new ArrayList<>(developers);
            Collections.shuffle(shuffledDevelopers, random);

            for (Developer dev : shuffledDevelopers) {
                // Ensure no more than 2 assignments per developer
                if (developerAssignments.get(dev) >= 2) continue;

                // Ensure no consecutive assignments
                int dayIndex = weekdays.indexOf(day);
                if (dayIndex > 0) {
                    String prevDay = weekdays.get(dayIndex - 1);
                    if (schedule.get(prevDay).contains(dev)) continue;
                }

                // Assign the developer
                schedule.get(day).add(dev);
                developerAssignments.put(dev, developerAssignments.get(dev) + 1);
                assigned.add(dev);

                // Stop once we have 3 developers for the day
                if (assigned.size() == 3) break;
            }
        }
    }

    // Pass the schedule to the model
    model.addAttribute("schedule", schedule);
    return "schedule";
}

    private boolean isAssignedToPreviousDay(
            Developer dev,
            String currentDay,
            List<String> weekdays,
            Map<String, List<Developer>> weekdayAssignments) {

        int currentIndex = weekdays.indexOf(currentDay);
        if (currentIndex == 0) return false; // No previous day for Monday

        String previousDay = weekdays.get(currentIndex - 1);
        return weekdayAssignments.get(previousDay).contains(dev);
    }


}
