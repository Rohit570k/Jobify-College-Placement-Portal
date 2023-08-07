package com.example.jobify.Models;

public class ApplyJobRequest {

//    {
//        "applied":"646f68abd3f549ea260ba1f0"
//    }

    private  String  applied;

    public ApplyJobRequest(String applied) {
        this.applied = applied;
    }

    public String getApplied() {
        return applied;
    }

    public void setApplied(String applied) {
        this.applied = applied;
    }
}
