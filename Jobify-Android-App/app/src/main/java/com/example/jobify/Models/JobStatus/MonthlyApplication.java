package com.example.jobify.Models.JobStatus;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MonthlyApplication {
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("count")
    @Expose
    private Integer count;

    public MonthlyApplication() {
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
