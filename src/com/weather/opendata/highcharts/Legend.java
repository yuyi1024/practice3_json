package com.weather.opendata.highcharts;

public class Legend {
    private String layout;
    private String align;
    private String verticalAlign;

    public Legend(String layout, String align, String verticalAlign) {
        this.layout = "\'" + layout + "\'";
        this.align = "\'" + align + "\'";
        this.verticalAlign = "\'" + verticalAlign + "\'";
    }
}
