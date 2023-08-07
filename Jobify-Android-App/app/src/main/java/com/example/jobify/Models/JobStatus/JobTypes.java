package com.example.jobify.Models.JobStatus;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class JobTypes {
    @SerializedName("fulltime")
    @Expose
    private Integer fulltime;
    @SerializedName("parttime")
    @Expose
    private Integer parttime;
    @SerializedName("remote")
    @Expose
    private Integer remote;
    @SerializedName("internship")
    @Expose
    private Integer internship;

    public Integer getFulltime() {
        return fulltime;
    }

    public void setFulltime(Integer fulltime) {
        this.fulltime = fulltime;
    }

    public Integer getParttime() {
        return parttime;
    }

    public void setParttime(Integer parttime) {
        this.parttime = parttime;
    }

    public Integer getRemote() {
        return remote;
    }

    public void setRemote(Integer remote) {
        this.remote = remote;
    }

    public Integer getInternship() {
        return internship;
    }

    public void setInternship(Integer internship) {
        this.internship = internship;
    }

}
