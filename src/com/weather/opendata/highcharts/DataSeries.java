package com.weather.opendata.highcharts;

import java.util.ArrayList;

public class DataSeries {
    private String name;
    private ArrayList<Integer> data;

    public DataSeries(String name, ArrayList<Integer> data) {
        this.name = name;
        this.data = data;
    }
}
