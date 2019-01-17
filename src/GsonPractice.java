import com.google.gson.Gson;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;

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
            ArrayList<WetherRow> wetherRows = new ArrayList<>();
            Gson gson = new Gson();

            //從 properties 檔取得 url
            Properties properties = new Properties();
            properties.load(new InputStreamReader(new FileInputStream("urlSetting.properties"), "UTF8"));
            String request_uri = properties.getProperty("request_uri");

            //取得 url 回傳資訊
            HttpClientRequest request = new HttpClientRequest(request_uri);
            String responseContext = request.getUriContext();

            // fromJson() parse 出的 Event
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
                        for(WetherRow row : wetherRows){
                            if (row.timeEquals(startTime, endTime)){
                                rowsIndex = wetherRows.indexOf(row);
                                break;
                            }
                        }
                        if(rowsIndex < 0){
                            wetherRows.add(new WetherRow());
                            rowsIndex = wetherRows.size() - 1;
                        }

                        wetherRows.get(rowsIndex).setStartTime(startTime);
                        wetherRows.get(rowsIndex).setEndTime(endTime);

                        switch(category){
                            case "Wx":
                                wetherRows.get(rowsIndex).setWx(parameterValue);
                                break;

                            case "PoP":
                                wetherRows.get(rowsIndex).setPop(parameterValue);
                                break;

                            case "MinT":
                                wetherRows.get(rowsIndex).setMinT(parameterValue);
                                break;

                            case "MaxT":
                                wetherRows.get(rowsIndex).setMaxT(parameterValue);
                                break;

                            default:
                                break;
                        }
                    }
                }
                for (WetherRow row : wetherRows){
                    query = "INSERT INTO measurement (location, start_time, finish_time, wx, pop, min_t, max_t) VALUES ('" + city + "', '" + row.getStartTime() + "', '" + row.getEndTime() + "', '" + row.getWx() + "', " + row.getPop() + ", " + row.getMinT() + ", " + row.getMaxT() + ");";
                    stmt.executeUpdate(query);
                    System.out.println(query);
                }
                wetherRows.clear();
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
