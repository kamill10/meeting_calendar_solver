package org.example;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Setter
@Getter
public class MeetingCalandarResolverAlgorithm {
    private int meetingLength;
    private TimeRange workingHours1;
    private TimeRange workingHours2;
    private PlannedMeeting plannedMeeting1;
    private PlannedMeeting plannedMeeting2;

    public MeetingCalandarResolverAlgorithm(int meetingLength, TimeRange workingHours1, TimeRange workingHours2
            , PlannedMeeting plannedMeeting1, PlannedMeeting plannedMeeting2) {
        checkIfPlannedMeetingInWorkingHours(plannedMeeting1, plannedMeeting2, workingHours1, workingHours2);
        this.meetingLength = meetingLength;
        this.workingHours1 = workingHours1;
        this.workingHours2 = workingHours2;
        this.plannedMeeting1 = plannedMeeting1;
        this.plannedMeeting2 = plannedMeeting2;
    }


    private LocalTime takeLaterStartWorkingHour(TimeRange workingHours1, TimeRange workingHours2) {
        if (workingHours1.getStart().getSecond() > workingHours2.getStart().getSecond()) {
            return workingHours1.getStart();
        }
        return workingHours2.getStart();
    }

    private LocalTime takeSoonerEndWorkingHour(TimeRange workingHours1, TimeRange workingHours2) {
        if (workingHours1.getEnd().getSecond() < workingHours2.getEnd().getSecond()) {
            return workingHours1.getStart();
        }
        return workingHours2.getEnd();
    }

    private boolean isAnyPlannedMeetingListNotOverlapsWithRange(TimeRange timeRangeLoop, PlannedMeeting plannedMeeting) {
        for (TimeRange timeRange : plannedMeeting.getMeetings()) {
            if (TimeRangeOverlapChecker.isTimeRangesOverlap(timeRangeLoop, timeRange)) {
                return false;
            }
        }
        return true;
    }

    public List<TimeRange> getPossibleTimeRanges() {
        //method for getting  possible time ranges taking into considerations working hours and planned meeting
        LocalTime start = takeLaterStartWorkingHour(workingHours1, workingHours2);
        LocalTime end = takeSoonerEndWorkingHour(workingHours1, workingHours2);
        //if we consider that working hours could begin with 1 minutes difference
        //only if working hours are with 30 min interval we can consider that the minutesToAdd is equal 30
        //This solution will always work but is not optimal
        int minutesToAdd = 1;
        TimeRange timeRangeLoop = new TimeRange(start, start.plusMinutes(meetingLength));
        List<TimeRange> possibleTimeRanges = new ArrayList<>();
        while (!timeRangeLoop.getEnd().isAfter(end) || timeRangeLoop.getEnd().equals(end)) {
            if (isAnyPlannedMeetingListNotOverlapsWithRange(timeRangeLoop, plannedMeeting1) &&
                    isAnyPlannedMeetingListNotOverlapsWithRange(timeRangeLoop, plannedMeeting2)) {
                possibleTimeRanges.add(timeRangeLoop);
            }
            LocalTime nextStart = timeRangeLoop.getStart().plusMinutes(minutesToAdd);
            timeRangeLoop = new TimeRange(nextStart, nextStart.plusMinutes(meetingLength));
        }
        return possibleTimeRanges;
    }

    public List<TimeRange> getPossibleTimeRangesJoined() {
        // Method to merge the possible time ranges
        List<TimeRange> possibleTimeRanges = getPossibleTimeRanges();
        List<TimeRange> mergedTimeRanges = new ArrayList<>();
        TimeRange currentRange = possibleTimeRanges.getFirst();
        for (int i = 1; i < possibleTimeRanges.size(); i++) {
            TimeRange nextRange = possibleTimeRanges.get(i);
            if (possibleTimeRanges.get(i-1).getStart().plusMinutes(1).equals(nextRange.getStart())) {
                // If the end time of the current range is exactly one minute before the start time of the next range, merge them
                currentRange = new TimeRange(currentRange.getStart(), nextRange.getEnd());
                if(i == possibleTimeRanges.size() - 1){
                    mergedTimeRanges.add(currentRange);
                }
            } else {
                // If they are not exactly adjacent, add the current range to the result list
                mergedTimeRanges.add(currentRange);
                currentRange = nextRange; // Move to the next range
            }
        }
        return mergedTimeRanges;
    }

    private void checkIfPlannedMeetingInWorkingHours(PlannedMeeting plannedMeeting1, PlannedMeeting plannedMeeting2
            , TimeRange workingHours1, TimeRange workingHours2) {
        // Check if all meetings in plannedMeeting1 are within workingHours1
        if (plannedMeeting1.getMeetings().stream().anyMatch(timeRange ->
                timeRange.getStart().isBefore(workingHours1.getStart()) || timeRange.getEnd().isAfter(workingHours1.getEnd()))) {
            throw new IllegalArgumentException("Planned meetings for the first person are not entirely within working hours");
        }
        // Check if all meetings in plannedMeeting2 are within workingHours2
        if (plannedMeeting2.getMeetings().stream().anyMatch(timeRange ->
                timeRange.getStart().isBefore(workingHours2.getStart()) || timeRange.getEnd().isAfter(workingHours2.getEnd()))) {
            throw new IllegalArgumentException("Planned meetings for the second person are not entirely within working hours");
        }
    }

}

