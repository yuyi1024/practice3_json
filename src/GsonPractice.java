import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.ArrayList;

public class GsonPractice {
    public static void main(String[] args){

        // connect to DB
        String url = "jdbc:sqlserver://localhost:1433;databasename=weather;integratedSecurity=true";
        String driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
        Statement stmt = null;
        Connection conn = null;
        String query = "";

        try {
            Class.forName(driver);  // return Class object
            conn = DriverManager.getConnection(url, "", "");
            stmt = conn.createStatement();
        } catch (Exception e) {
            System.out.println("Something throws the exception when connecting to DB.");
            e.printStackTrace();
        }

        try {
            String city = "";
            String category = "";
            String parameterValue = "";
            String startTime = "";
            String endTime = "";
            String[] begin = new String[3];
            String[] finish = new String[3];
            String[] wx = new String[3];
            String[] pop = new String[3];
            String[] minT = new String[3];
            String[] maxT = new String[3];
            int timeIntervelIndex = -1;

            InputStreamReader inputFile = new InputStreamReader(new FileInputStream("test1.json"), "UTF8");
            JsonReader jsonReader = new JsonReader(inputFile);

            Gson gson = new Gson();

            // fromJson() parse 出的 object 用 arrayList 去接
            TypeToken<ArrayList<Event>> type = new TypeToken<ArrayList<Event>>(){};     //ArrayList<Event>
            ArrayList<Event> jsonArray = gson.fromJson(jsonReader, type.getType());

            ArrayList<Location> locations = jsonArray.get(0).getRecords().getLocation();

            for (Location location : locations){
                city = location.getLocationName();    //嘉義市/...

                for (WeatherElement weatherElement : location.getWeatherElement()){
                    category = weatherElement.getElementName();   //Wx/POP/Tmax/Tmin

                    for (Time time : weatherElement.getTime()){
                        parameterValue = time.getParameter().getParameterName();
                        startTime = time.getStartTime();
                        endTime = time.getEndTime();

                        if(startTime.equals("2019-01-15 00:00:00") && endTime.equals("2019-01-15 06:00:00")){
                            timeIntervelIndex = 0;
                        } else if(startTime.equals("2019-01-15 06:00:00") && endTime.equals("2019-01-15 18:00:00")){
                            timeIntervelIndex = 1;
                        } else if(startTime.equals("2019-01-15 18:00:00") && endTime.equals("2019-01-16 06:00:00")){
                            timeIntervelIndex = 2;
                        }

                        switch(category){
                            case "Wx":
                                wx[timeIntervelIndex] = parameterValue;
                                begin[timeIntervelIndex] = startTime;
                                finish[timeIntervelIndex] = endTime;
                                break;

                            case "PoP":
                                pop[timeIntervelIndex] = parameterValue;
                                break;

                            case "MinT":
                                minT[timeIntervelIndex] = parameterValue;
                                break;

                            case "MaxT":
                                maxT[timeIntervelIndex] = parameterValue;
                                break;

                            default:
                                break;
                        }
                    }
                }
                for (int j = 0; j < 3; j++){
                    query = "INSERT INTO measurement (location, start_time, finish_time, wx, pop, min_t, max_t) VALUES ('" + city + "', '" + begin[j] + "', '" + finish[j] + "', '" + wx[j] + "', " + pop[j] + ", " + minT[j] + ", " + maxT[j] + ");";
                    stmt.executeUpdate(query);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
