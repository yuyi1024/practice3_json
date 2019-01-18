package com.weather.opendata.highcharts;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Series {
    private String pointStart;
    private Long pointInterval;

    public Series(Date pointStart,  Long pointInterval) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy, M, d, H, m, s");
        String pt = df.format(pointStart);

        this.pointStart = "Date.UTC(" + pt + ")";
        this.pointInterval = pointInterval;
    }
}
