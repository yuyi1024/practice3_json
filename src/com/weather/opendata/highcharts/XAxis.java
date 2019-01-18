package com.weather.opendata.highcharts;

public class XAxis {
    private Title title;
    private String type;
    private DateTimeLabelFormats dateTimeLabelFormats;

    public XAxis(Title title, String type, DateTimeLabelFormats dateTimeLabelFormats) {
        this.title = title;
        this.type = "'" + type + "'";
        this.dateTimeLabelFormats = dateTimeLabelFormats;
    }
}
