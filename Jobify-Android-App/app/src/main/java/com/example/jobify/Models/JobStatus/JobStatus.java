package com.example.jobify.Models.JobStatus;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class JobStatus {
    @SerializedName("status")
    @Expose
    private Status status;
    @SerializedName("jobTypes")
    @Expose
    private JobTypes jobTypes;
    @SerializedName("monthlyApplications")
    @Expose
    private List<MonthlyApplication> monthlyApplications;

    public JobStatus() {
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public JobTypes getJobTypes() {
        return jobTypes;
    }

    public void setJobTypes(JobTypes jobTypes) {
        this.jobTypes = jobTypes;
    }

    public List<MonthlyApplication> getMonthlyApplications() {
        return monthlyApplications;
    }

    public void setMonthlyApplications(List<MonthlyApplication> monthlyApplications) {
        this.monthlyApplications = monthlyApplications;
    }

}
