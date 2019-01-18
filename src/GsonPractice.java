import com.google.gson.Gson;
import com.weather.opendata.*;
import com.weather.opendata.highcharts.*;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Properties;

public class GsonPractice {
    public static void main(String[] args) throws UnsupportedEncodingException {

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
            ArrayList<WeatherRow> weatherRows = new ArrayList<>();
            Gson gson = new Gson();

            //從 properties 檔取得 url
            Properties properties = new Properties();
            properties.load(new InputStreamReader(new FileInputStream("urlSetting.properties"), "UTF8"));
            String request_uri = properties.getProperty("request_uri");

            //取得 url 回傳資訊
            HttpClientRequest request = new HttpClientRequest(request_uri);
            String responseContext = request.getUriContext();

            // fromJson() parse 出的 com.weather.opendata.Event
            Event event =gson.fromJson(responseContext, Event.class);

            ArrayList<Location> locations = event.getRecords().getLocation();

            for (Location location : locations){
                city = location.getLocationName();    //嘉義市/...

                for (WeatherElement weatherElement : location.getWeatherElement()){
                    category = weatherElement.getElementName();   //Wx/POP/Tmax/Tmin

                    for (Time time : weatherElement.getTime()){
                        parameterValue = time.getParameter().getParameterName();    //value
                        startTime = time.getStartTime();    //開始時間
                        endTime = time.getEndTime();    //結束時間
                        int rowsIndex = -1; //ArrayList 定位

                        //判斷是否已存在時間區間相同的 WetherRow，若否則 new 一個
                        for(WeatherRow row : weatherRows){
                            if (row.timeEquals(startTime, endTime)){
                                rowsIndex = weatherRows.indexOf(row);
                                break;
                            }
                        }
                        if(rowsIndex < 0){
                            weatherRows.add(new WeatherRow());
                            rowsIndex = weatherRows.size() - 1;
                        }

                        weatherRows.get(rowsIndex).setStartTime(startTime);
                        weatherRows.get(rowsIndex).setEndTime(endTime);

                        switch(category){
                            case "Wx":
                                weatherRows.get(rowsIndex).setWx(parameterValue);
                                break;

                            case "PoP":
                                weatherRows.get(rowsIndex).setPop(parameterValue);
                                break;

                            case "MinT":
                                weatherRows.get(rowsIndex).setMinT(parameterValue);
                                break;

                            case "MaxT":
                                weatherRows.get(rowsIndex).setMaxT(parameterValue);
                                break;

                            default:
                                break;
                        }
                    }
                }
                for (WeatherRow row : weatherRows){
                    query = "INSERT INTO measurement (location, start_time, finish_time, wx, pop, min_t, max_t) VALUES ('" + city + "', '" + row.getStartTime() + "', '" + row.getEndTime() + "', '" + row.getWx() + "', " + row.getPop() + ", " + row.getMinT() + ", " + row.getMaxT() + ");";
//                    stmt.executeUpdate(query);
//                    System.out.println(query);
                }
                weatherRows.clear();
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

        Calendar c =Calendar.getInstance();
        c.add(Calendar.DATE, -10);


//        pointStart:Date.UTC(2010, 0, 1, 12, 4,5),

        ArrayList<Integer> data = new ArrayList<>();
        data.add(4);
        data.add(5);
        data.add(1);
        data.add(2);
        data.add(5);


        ArrayList<DataSeries> series = new ArrayList<>();
        series.add(new DataSeries("Installation", data));
        series.add(new DataSeries("Manufacturing", data));
        series.add(new DataSeries("Sales & Distribution", data));


        Chart line = new Chart(new Title("17~18日氣象預測折線圖"),
                               new Subtitle("冷冷"),
                               new XAxis(new Title("時間")),
                               new YAxis(new Title("度C")),
                               new Legend("vertical", "right", "middle"),
                               new PlotOptions(new Series(c.getTime(), new Long(24 * 60 * 60 * 1000))),
                               series
                               );

        Gson gson = new Gson();
        String x = gson.toJson(line);

        x = x.replaceAll("\"", "");
        x = x.replaceAll("#", "'");

        System.out.println(x);














    }
}
