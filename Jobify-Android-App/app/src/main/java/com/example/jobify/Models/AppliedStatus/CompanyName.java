package com.example.jobify.Models.AppliedStatus;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CompanyName {

    @SerializedName("firstround")
    @Expose
    private List<String> firstround;
    @SerializedName("secondround")
    @Expose
    private List<String> secondround;
    @SerializedName("interview")
    @Expose
    private List<String> interview;
    @SerializedName("selected")
    @Expose
    private List<String> selected;

    public List<String> getFirstround() {
        return firstround;
    }

    public void setFirstround(List<String> firstround) {
        this.firstround = firstround;
    }

    public List<String> getSecondround() {
        return secondround;
    }

    public void setSecondround(List<String> secondround) {
        this.secondround = secondround;
    }

    public List<String> getInterview() {
        return interview;
    }

    public void setInterview(List<String> interview) {
        this.interview = interview;
    }

    public List<String> getSelected() {
        return selected;
    }

    public void setSelected(List<String> selected) {
        this.selected = selected;
    }

}
