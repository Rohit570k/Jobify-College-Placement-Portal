package com.example.jobify.Models;

import java.util.List;

public class AppliedJobResponse {
    private List<AppliedApplication> myApplications;
    private int totalApplication;

    public List<AppliedApplication> getMyApplications() {
        return myApplications;
    }

    public void setMyApplications(List<AppliedApplication> myApplications) {
        this.myApplications = myApplications;
    }

    public int getTotalApplication() {
        return totalApplication;
    }

    public void setTotalApplication(int totalApplication) {
        this.totalApplication = totalApplication;
    }
}
