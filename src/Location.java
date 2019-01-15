import java.util.ArrayList;

public class Location {
    private String locationName;
    private ArrayList<WeatherElement> weatherElement;

    public Location(String locationName, ArrayList<WeatherElement> weatherElement) {
        this.locationName = locationName;
        this.weatherElement = weatherElement;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public ArrayList<WeatherElement> getWeatherElement() {
        return weatherElement;
    }

    public void setWeatherElement(ArrayList<WeatherElement> weatherElement) {
        this.weatherElement = weatherElement;
    }
}
