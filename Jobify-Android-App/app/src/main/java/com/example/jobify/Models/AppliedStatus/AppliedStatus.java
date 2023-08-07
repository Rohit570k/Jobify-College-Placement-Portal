package com.example.jobify.Models.AppliedStatus;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AppliedStatus {

    @SerializedName("status")
    @Expose
    private MyStatus status;
    @SerializedName("companyName")
    @Expose
    private CompanyName companyName;

    public MyStatus getMyStatus() {
        return status;
    }

    public void setMyStatus(MyStatus status) {
        this.status = status;
    }

    public CompanyName getCompanyName() {
        return companyName;
    }

    public void setCompanyName(CompanyName companyName) {
        this.companyName = companyName;
    }
}
