package org.example;


public class TimeRangeOverlapChecker {
    public static boolean isTimeRangesOverlap(TimeRange timeRange1, TimeRange timeRange2){
        return timeRange1.getEnd().isAfter(timeRange2.getStart()) &&
                timeRange1.getStart().isBefore(timeRange2.getEnd());
    }
}
