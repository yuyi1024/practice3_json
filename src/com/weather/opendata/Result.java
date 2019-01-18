package com.weather.opendata;

import java.util.ArrayList;

public class Result {
    private String resource_id;
    ArrayList<Column> fields;

    public Result(String resource_id, ArrayList<Column> fields) {
        this.resource_id = resource_id;
        this.fields = fields;
    }

    public String getResource_id() {
        return resource_id;
    }

    public void setResource_id(String resource_id) {
        this.resource_id = resource_id;
    }

    public ArrayList<Column> getFields() {
        return fields;
    }

    public void setFields(ArrayList<Column> fields) {
        this.fields = fields;
    }

    public static class Data {
    }
}
