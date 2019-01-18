package com.weather.opendata;

import java.util.ArrayList;

public class Records {
    private String datasetDescription;
    private ArrayList<Location> location;

    public Records(String datasetDescription, ArrayList<Location> location) {
        this.datasetDescription = datasetDescription;
        this.location = location;
    }


    public String getDatasetDescription() {
        return datasetDescription;
    }

    public void setDatasetDescription(String datasetDescription) {
        this.datasetDescription = datasetDescription;
    }

    public ArrayList<Location> getLocation() {
        return location;
    }

    public void setLocation(ArrayList<Location> location) {
        this.location = location;
    }
}
