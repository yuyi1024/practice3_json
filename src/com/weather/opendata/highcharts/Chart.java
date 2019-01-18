package com.weather.opendata.highcharts;

import java.util.ArrayList;

public class Chart {
    private Title title;
    private Subtitle subtitle;
    private XAxis xAxis;
    private YAxis yAxis;
    private Legend legend;
    private PlotOptions plotOptions;
    private ArrayList<DataSeries> series;

    public Chart(Title title, Subtitle subtitle, XAxis xAxis, YAxis yAxis, Legend legend,
                 PlotOptions plotOptions, ArrayList<DataSeries> series) {
        this.title = title;
        this.subtitle = subtitle;
        this.xAxis = xAxis;
        this.yAxis = yAxis;
        this.legend = legend;
        this.plotOptions = plotOptions;
        this.series = series;
    }
}
