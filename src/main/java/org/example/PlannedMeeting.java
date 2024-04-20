package org.example;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class PlannedMeeting {
    private List<TimeRange> meetings = new ArrayList<>();
    PlannedMeeting(){};
    public PlannedMeeting(List<TimeRange> meetings){
        this.meetings = meetings;
    }
    public void addMeeting(TimeRange meeting){
        meetings.add(meeting);
    }
    public void removeMeeting(TimeRange meeting){
        meetings.remove(meeting);
    }
}
