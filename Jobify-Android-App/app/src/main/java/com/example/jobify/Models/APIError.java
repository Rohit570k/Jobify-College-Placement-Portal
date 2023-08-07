package com.example.jobify.Models;

public class APIError {
    private int statusCode;
    private String msg;

    public APIError() {
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getMsg() {
        return msg;
    }
}
