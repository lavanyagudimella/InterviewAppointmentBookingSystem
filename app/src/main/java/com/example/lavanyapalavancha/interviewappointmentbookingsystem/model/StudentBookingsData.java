package com.example.lavanyapalavancha.interviewappointmentbookingsystem.model;

import java.util.ArrayList;

/**
 * Created by lavanyapalavancha on 10/21/17.
 */

public class StudentBookingsData {
    private String studentName;
    private String studentNumber;
    private String contactEmail;
    private String workDescription;
    private String interviewTimings;

    public StudentBookingsData(String studentName, String studentNumber, String contactEmail, String workDescription, String interviewTimings) {
        this.studentName = studentName;
        this.studentNumber = studentNumber;
        this.contactEmail = contactEmail;
        this.workDescription = workDescription;
        this.interviewTimings = interviewTimings;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getWorkDescription() {
        return workDescription;
    }

    public void setWorkDescription(String workDescription) {
        this.workDescription = workDescription;
    }

    public String getInterviewTimings() {
        return interviewTimings;
    }

    public void setInterviewTimings(String interviewTimings) {
        this.interviewTimings = interviewTimings;
    }
}
