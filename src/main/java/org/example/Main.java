package org.example;

import org.example.MeetingCalandarResolverAlgorithm;
import org.example.PlannedMeeting;
import org.example.TimeRange;

import java.time.LocalTime;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        PlannedMeeting plannedMeeting1 = new PlannedMeeting();
        plannedMeeting1.addMeeting(new TimeRange(LocalTime.of(9, 2), LocalTime.of(10, 30)));
        plannedMeeting1.addMeeting(new TimeRange(LocalTime.of(12, 0), LocalTime.of(13, 0)));
        plannedMeeting1.addMeeting(new TimeRange(LocalTime.of(16, 0), LocalTime.of(18, 0)));
        PlannedMeeting plannedMeeting2 = new PlannedMeeting();
        plannedMeeting2.addMeeting(new TimeRange(LocalTime.of(10, 0), LocalTime.of(11, 30)));
        plannedMeeting2.addMeeting(new TimeRange(LocalTime.of(12, 30), LocalTime.of(14, 30)));
        plannedMeeting2.addMeeting(new TimeRange(LocalTime.of(14, 30), LocalTime.of(15, 0)));
        plannedMeeting2.addMeeting(new TimeRange(LocalTime.of(16, 0), LocalTime.of(17, 0)));

        TimeRange workingHours1 = new TimeRange(LocalTime.of(9, 0), LocalTime.of(19, 55));
        TimeRange workingHours2 = new TimeRange(LocalTime.of(9, 0), LocalTime.of(18, 30));
        int meetingLenght = 30;
        MeetingCalandarResolverAlgorithm meetingCalandarResolverAlgorithm = new MeetingCalandarResolverAlgorithm
                (meetingLenght, workingHours1, workingHours2, plannedMeeting1, plannedMeeting2);
        meetingCalandarResolverAlgorithm.getPossibleTimeRangesJoined().forEach(System.out::println);
          /*
        You can get all data from user input
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter working hours for the first person:");
        TimeRange workingHours1 = getTimeRangeFromUser(scanner);
        System.out.println("Enter working hours for the second person:");
        TimeRange workingHours2 = getTimeRangeFromUser(scanner);
        System.out.println("Enter meeting length (in minutes):");
        int meetingLength = scanner.nextInt();
        System.out.println("Enter planned meetings for the first person:");
        PlannedMeeting plannedMeeting1 = getPlannedMeetingFromUser(scanner);
        System.out.println("Enter planned meetings for the second person:");
        PlannedMeeting plannedMeeting2 = getPlannedMeetingFromUser(scanner);
        MeetingCalandarResolverAlgorithm meetingCalandarResolverAlgorithm = new MeetingCalandarResolverAlgorithm(
                meetingLength,
                workingHours1,
                workingHours2,
                plannedMeeting1,
                plannedMeeting2
        );
        System.out.println("Possible time ranges for scheduling meetings:");
        meetingCalandarResolverAlgorithm.getPossibleTimeRanges().forEach(System.out::println);
        scanner.close();*/
    }
    private static TimeRange getTimeRangeFromUser(Scanner scanner) {
        System.out.println("Enter start time (HH:MM):");
        LocalTime startTime = LocalTime.parse(scanner.next());
        System.out.println("Enter end time (HH:MM):");
        LocalTime endTime = LocalTime.parse(scanner.next());
        return new TimeRange(startTime, endTime);
    }
    private static PlannedMeeting getPlannedMeetingFromUser(Scanner scanner) {
        PlannedMeeting plannedMeeting = new PlannedMeeting();
        boolean addAnotherMeeting = true;
        while (addAnotherMeeting) {
            System.out.println("Enter start time of planned meeting (HH:MM):");
            LocalTime startTime = LocalTime.parse(scanner.next());
            System.out.println("Enter end time of planned meeting (HH:MM):");
            LocalTime endTime = LocalTime.parse(scanner.next());
            plannedMeeting.addMeeting(new TimeRange(startTime, endTime));

            System.out.println("Do you want to add another planned meeting? (yes/no):");
            String response = scanner.next().toLowerCase();
            addAnotherMeeting = response.equals("yes");
        }
        return plannedMeeting;
    }
}
