package model;

import java.time.LocalDateTime;

public class Interview {
    private int interviewId;
    private int applicationId;
    private String roundName;
    private LocalDateTime scheduleTime;
    private String notes;

    // used when adding a new interview
    public Interview(int applicationId, String roundName,
                     LocalDateTime scheduleTime, String notes) {
        this.applicationId = applicationId;
        this.roundName = roundName;
        this.scheduleTime = scheduleTime;
        this.notes = notes;
    }

    // used when reading an interview from the database
    public Interview(int interviewId, int applicationId, String roundName,
                     LocalDateTime scheduleTime, String notes) {
        this.interviewId = interviewId;
        this.applicationId = applicationId;
        this.roundName = roundName;
        this.scheduleTime = scheduleTime;
        this.notes = notes;
    }

    public int getInterviewId() { return interviewId; }
    public int getApplicationId() { return applicationId; }
    public String getRoundName() { return roundName; }
    public LocalDateTime getScheduleTime() { return scheduleTime; }
    public String getNotes() { return notes; }

    public void setInterviewId(int interviewId) { this.interviewId = interviewId; }
    public void setApplicationId(int applicationId) { this.applicationId = applicationId; }
    public void setRoundName(String roundName) { this.roundName = roundName; }
    public void setScheduleTime(LocalDateTime scheduleTime) { this.scheduleTime = scheduleTime; }
    public void setNotes(String notes) { this.notes = notes; }

    @Override
    public String toString() {
        return interviewId + " | application " + applicationId + " | " + roundName
                + " | " + scheduleTime + " | " + notes;
    }
}