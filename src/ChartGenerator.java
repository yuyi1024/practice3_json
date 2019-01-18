import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.weather.opendata.highcharts.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;

public class ChartGenerator {
    public static void main(String[] args){
        // connect to DB
        String url = "jdbc:sqlserver://localhost:1433;databasename=weather;integratedSecurity=true";
        String driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
        Statement stmt = null;
        Statement stmt2 = null;
        Connection conn = null;
        String query = "";

        try {
            Class.forName(driver);  // return Class object
            conn = DriverManager.getConnection(url, "", "");
            stmt = conn.createStatement();
            stmt2 = conn.createStatement();
        } catch (Exception e) {
            System.out.println("Something throws the exception when connecting to DB.");
            e.printStackTrace();
        }

        try {
            //HighChart startTime
            Calendar calendar =Calendar.getInstance();
            calendar.set(Calendar.HOUR, 6);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);

            ResultSet allLocation = stmt.executeQuery("SELECT DISTINCT location FROM measurement");

            ArrayList<Integer> dataMaxT = new ArrayList<>();
            ArrayList<Integer> dataMinT = new ArrayList<>();
            ArrayList<DataSeries> maxTSeries = new ArrayList<>();
            ArrayList<DataSeries> minTSeries = new ArrayList<>();
            String location = "";

            while (allLocation.next()){
                location = allLocation.getString("location");
                query = "SELECT min_t, max_t FROM measurement WHERE location = '" + location + "' ORDER BY start_time ASC";
                ResultSet rs = stmt2.executeQuery(query);
                while (rs.next()){
                    dataMaxT.add(rs.getInt("max_t"));
                    dataMinT.add(rs.getInt("min_t"));
                }
                maxTSeries.add(new DataSeries(location, (ArrayList<Integer>) dataMaxT.clone()));
                minTSeries.add(new DataSeries(location, (ArrayList<Integer>) dataMinT.clone()));
                dataMaxT.clear();
                dataMinT.clear();
            }

            Chart maxTChart = new Chart(new Title("近兩日最高氣溫預測折線圖"),
                                        new Subtitle("聖誕快樂"),
                                        new XAxis(new Title("時間"), "datetime", new DateTimeLabelFormats("%b %e-%H:%M")),
                                        new YAxis(new Title("溫度(°C)")),
                                        new Legend("vertical", "right", "middle"),
                                        new PlotOptions(new Series(calendar.getTime(), new Long(12 * 60 * 60 * 1000))),
                                        maxTSeries
            );

            Chart minTChart = new Chart(new Title("近兩日最低氣溫預測折線圖"),
                                        new Subtitle("元宵快樂"),
                                        new XAxis(new Title("時間"), "datetime", new DateTimeLabelFormats("%b %e-%H:%M")),
                                        new YAxis(new Title("溫度(°C)")),
                                        new Legend("vertical", "right", "middle"),
                                        new PlotOptions(new Series(calendar.getTime(), new Long(12 * 60 * 60 * 1000))),
                                        minTSeries
            );

            Gson gson = new GsonBuilder().disableHtmlEscaping().create();
            String maxTJson = gson.toJson(maxTChart);
            String minTJson = gson.toJson(minTChart);
            maxTJson = maxTJson.replaceAll("\"", "");
            minTJson = minTJson.replaceAll("\"", "");
            System.out.println(maxTJson);
            System.out.println(minTJson);

        } catch (SQLException e) {
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
