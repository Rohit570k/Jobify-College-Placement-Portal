package com.example.jobify.Models.JobStatus;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Status {
    @SerializedName("ongoing")
    @Expose
    private Integer ongoing;
    @SerializedName("completed")
    @Expose
    private Integer completed;
    @SerializedName("upcoming")
    @Expose
    private Integer upcoming;

    public Status() {
    }

    public Integer getOngoing() {
        return ongoing;
    }

    public void setOngoing(Integer ongoing) {
        this.ongoing = ongoing;
    }

    public Integer getCompleted() {
        return completed;
    }

    public void setCompleted(Integer completed) {
        this.completed = completed;
    }

    public Integer getUpcoming() {
        return upcoming;
    }

    public void setUpcoming(Integer upcoming) {
        this.upcoming = upcoming;
    }

}
