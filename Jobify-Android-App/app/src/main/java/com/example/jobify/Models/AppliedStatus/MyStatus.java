package com.example.jobify.Models.AppliedStatus;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MyStatus {
    @SerializedName("firstround")
    @Expose
    private Integer firstround;
    @SerializedName("secondround")
    @Expose
    private Integer secondround;
    @SerializedName("interview")
    @Expose
    private Integer interview;
    @SerializedName("selected")
    @Expose
    private Integer selected;

    public Integer getFirstround() {
        return firstround;
    }

    public void setFirstround(Integer firstround) {
        this.firstround = firstround;
    }

    public Integer getSecondround() {
        return secondround;
    }

    public void setSecondround(Integer secondround) {
        this.secondround = secondround;
    }

    public Integer getInterview() {
        return interview;
    }

    public void setInterview(Integer interview) {
        this.interview = interview;
    }

    public Integer getSelected() {
        return selected;
    }

    public void setSelected(Integer selected) {
        this.selected = selected;
    }

}
