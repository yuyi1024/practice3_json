package com.weather.opendata.highcharts;

import java.util.ArrayList;

public class DataSeries {
    private String name;
    private ArrayList<Integer> data;

    public DataSeries(String name, ArrayList<Integer> data) {
        this.name = "\'" + name + "\'";
        this.data = data;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Integer> getData() {
        return data;
    }

    public void setData(ArrayList<Integer> data) {
        this.data = data;
    }
}
