package com.weather.opendata;

import java.util.ArrayList;

public class WeatherElement {
    private String elementName;
    private ArrayList<Time> time;

    public WeatherElement(String elementName, ArrayList<Time> time) {
        this.elementName = elementName;
        this.time = time;
    }

    public String getElementName() {
        return elementName;
    }

    public void setElementName(String elementName) {
        this.elementName = elementName;
    }

    public ArrayList<Time> getTime() {
        return time;
    }

    public void setTime(ArrayList<Time> time) {
        this.time = time;
    }
}
