package com.example.lavanyapalavancha.interviewappointmentbookingsystem.model;

/**
 * Created by lavanyapalavancha on 10/20/17.
 */

public class InterviewAnnouncementData {
    private String unitcode;
    private String unitGrade;
    private String date;
    private String timings;
    private String room;
    private String announcement;

    public InterviewAnnouncementData(String unitcode, String unitGrade, String date, String timings, String room, String announcement) {
        this.unitcode = unitcode;
        this.unitGrade = unitGrade;
        this.date = date;
        this.timings = timings;
        this.room = room;
        this.announcement = announcement;
    }

    public String getUnitcode() {
        return unitcode;
    }

    public void setUnitcode(String unitcode) {
        this.unitcode = unitcode;
    }

    public String getUnitGrade() {
        return unitGrade;
    }

    public void setUnitGrade(String unitGrade) {
        this.unitGrade = unitGrade;
    }

    public String getAnnouncement() {
        return announcement;
    }

    public void setAnnouncement(String announcement) {
        this.announcement = announcement;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getTimings() {
        return timings;
    }

    public void setTimings(String timings) {
        this.timings = timings;
    }
}
