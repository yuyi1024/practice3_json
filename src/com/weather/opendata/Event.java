package com.weather.opendata;

public class Event {
    private String success;
    private Result result;
    private Records records;

    public Event(String success, Result result, Records records) {
        this.success = success;
        this.result = result;
        this.records = records;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public Records getRecords() {
        return records;
    }

    public void setRecords(Records records) {
        this.records = records;
    }
}
