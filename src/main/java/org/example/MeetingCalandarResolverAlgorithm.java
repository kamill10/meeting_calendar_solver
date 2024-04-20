package org.example;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;
import java.util.ArrayList;
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
        TimeRange timeRangeLoop = new TimeRange(start, start.plusMinutes(minutesToAdd));
        List<TimeRange> possibleTimeRanges = new ArrayList<>();
        while (!timeRangeLoop.getEnd().isAfter(end) || timeRangeLoop.getEnd().equals(end)) {
            if (isAnyPlannedMeetingListNotOverlapsWithRange(timeRangeLoop, plannedMeeting1) &&
                    isAnyPlannedMeetingListNotOverlapsWithRange(timeRangeLoop, plannedMeeting2)) {
                possibleTimeRanges.add(timeRangeLoop);
            }
            timeRangeLoop = new TimeRange(timeRangeLoop.getStart().plusMinutes(minutesToAdd), timeRangeLoop.getEnd().plusMinutes(minutesToAdd));
        }
        return possibleTimeRanges;
    }

    public List<TimeRange> getPossibleTimeRangesJoined() {
        //method to merge the possible time ranges
        List<TimeRange> possibleTimeRanges = getPossibleTimeRanges();
        List<TimeRange> mergedTimeRanges = new ArrayList<>();

        TimeRange currentRange = possibleTimeRanges.get(0);
        for (int i = 1; i < possibleTimeRanges.size(); i++) {
            TimeRange nextRange = possibleTimeRanges.get(i);
            if (currentRange.getEnd().equals(nextRange.getStart())) {
                // If the current range ends where the next range starts, merge them
                currentRange = new TimeRange(currentRange.getStart(), nextRange.getEnd());
            } else {
                // If they are not adjacent, add the current range to the result list

                if(isValidTimeRange(currentRange)){
                    mergedTimeRanges.add(currentRange);
                }
                currentRange = nextRange; // Move to the next range
            }
        }
        if(isValidTimeRange(currentRange)){
            mergedTimeRanges.add(currentRange);
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
    private boolean isValidTimeRange(TimeRange timeRange) {
        int startMinutes = timeRange.getStart().getHour() * 60 + timeRange.getStart().getMinute();
        int endMinutes = timeRange.getEnd().getHour() * 60 + timeRange.getEnd().getMinute();
        int duration = endMinutes - startMinutes;
        return duration >= meetingLength;
    }
}

