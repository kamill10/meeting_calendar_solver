package org.example;

import lombok.Getter;
import lombok.ToString;

import java.time.LocalTime;

@Getter
@ToString
public class TimeRange {
    private final LocalTime start;
    private final LocalTime end;

    public TimeRange(LocalTime start, LocalTime end){
        if(start == null || end == null){
            throw new IllegalArgumentException("Start and end time must be provided");
        }
        if (!start.isBefore(end)) {
            throw new IllegalArgumentException("Start time must be before end time");
        }
        this.start = start;
        this.end = end;
    }
}
