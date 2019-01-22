import com.google.gson.Gson;
import com.weather.opendata.*;
import com.weather.opendata.Time;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;


public class GsonPractice {
    public static void main(String[] args){
        Logger logger = LogManager.getLogger("GsonPractice");

        // connect to DB
        String url = "jdbc:sqlserver://localhost:1433;databasename=weather;integratedSecurity=true";
        String driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
        PreparedStatement stmt1 = null;
        PreparedStatement stmt2 = null;

        Connection conn = null;
        String query = "";

        try {
            Class.forName(driver);  // return Class object
            conn = DriverManager.getConnection(url, "", "");
//            stmt = conn.createStatement();
            logger.debug("DB connect succeed.");
        } catch (Exception e) {
            logger.error("Something throws the exception when connecting to DB.", e);
        }

        try {
            String city = "";
            String category = "";
            String parameterValue = "";
            String startTime = "";
            String endTime = "";
            ArrayList<WeatherRow> weatherRows = new ArrayList<>();
            Gson gson = new Gson();
            String request_uri = null;
            HashMap<String, WeatherRow> weatherMap = new HashMap<>();
            String timeKey = "";
            WeatherRow weatherRow = null;
            ResultSet rs = null;

            try {
                //從 properties 檔取得 url
                Properties properties = new Properties();
                properties.load(new InputStreamReader(new FileInputStream("urlSetting.properties"), "UTF8"));
                request_uri = properties.getProperty("request_uri");
                logger.debug("Input properties file succeed.");
            } catch (IOException e) {
                logger.error("Something throws the exception when input the properties file.", e);
            }

            //取得 url 回傳資訊
            HttpClientRequest request = new HttpClientRequest(request_uri);
            String responseContext = request.getUriContext(logger);

            // fromJson() parse 出的 com.weather.opendata.Event
            Event event =gson.fromJson(responseContext, Event.class);

            ArrayList<Location> locations = event.getRecords().getLocation();

            for (Location location : locations){
                city = location.getLocationName();    //嘉義市/...
                logger.debug("Location: " + city);

                for (WeatherElement weatherElement : location.getWeatherElement()){
                    category = weatherElement.getElementName();   //Wx/POP/Tmax/Tmin

                    for (Time time : weatherElement.getTime()){
                        parameterValue = time.getParameter().getParameterName();    //value
                        startTime = time.getStartTime();    //開始時間
                        endTime = time.getEndTime();    //結束時間

                        //判斷是否已存在時間區間相同的 WetherRow，若否則 new 一個
                        timeKey = startTime + "~" + endTime;
                        if(weatherMap.containsKey(timeKey)){
                            weatherRow = weatherMap.get(timeKey);
                        } else{
                            weatherRow = new WeatherRow();
                            weatherRow.setStartTime(startTime);
                            weatherRow.setEndTime(endTime);
                        }

                        switch(category){
                            case "Wx":
                                weatherRow.setWx(parameterValue);
                                break;

                            case "PoP":
                                weatherRow.setPop(parameterValue);
                                break;

                            case "MinT":
                                weatherRow.setMinT(parameterValue);
                                break;

                            case "MaxT":
                                weatherRow.setMaxT(parameterValue);
                                break;

                            default:
                                break;
                        }
                        weatherMap.put(timeKey, weatherRow);
                    }
                }

                for (WeatherRow w : weatherMap.values()){
                    query = "SELECT * FROM measurement WHERE location = ? AND start_time = ? AND finish_time = ?";
                    stmt1 = conn.prepareStatement(query);
                    stmt1.setString(1, city);
                    stmt1.setString(2, w.getStartTime());
                    stmt1.setString(3, w.getEndTime());
                    rs = stmt1.executeQuery();

                    if(!rs.next()){
                        System.out.println("YYYY");
                        query = "INSERT INTO measurement (location, start_time, finish_time, wx, pop, min_t, max_t) VALUES (?, ?, ?, ?, ?, ?, ?);";
                        stmt2 = conn.prepareStatement(query);
                        stmt2.setString(1, city);
                        stmt2.setString(2, w.getStartTime());
                        stmt2.setString(3, w.getEndTime());
                        stmt2.setString(4, w.getWx());
                        stmt2.setString(5, w.getPop());
                        stmt2.setString(6, w.getMinT());
                        stmt2.setString(7, w.getMaxT());
                        stmt2.execute();
                    }
                }
            }
            conn.close();
            logger.debug("All data insert succeed.");
        } catch (Exception e) {
            logger.error("Something throws the exception.", e);
        }
    }
}
