import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.AllowAllHostnameVerifier;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.TrustAllStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.SecureRandom;
import java.security.Security;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.ArrayList;

public class GsonPractice {
    public static void main(String[] args) throws Exception{

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
            int timeIntervelIndex = 0;

            HttpClientRequest hcr = new HttpClientRequest("https://opendata.cwb.gov.tw/api/v1/rest/datastore/F-C0032-001?Authorization=rdec-key-123-45678-011121314");
            String sjson = hcr.getUriContext();
            System.out.println(sjson);

//            InputStreamReader inputFile = new InputStreamReader(new FileInputStream("test1.json"), "UTF8");
//            JsonReader jsonReader = new JsonReader(inputFile);

            Gson gson = new Gson();

            // fromJson() parse 出的 Event
            Event event =gson.fromJson(sjson, Event.class);

            ArrayList<Location> locations = event.getRecords().getLocation();

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
//                    stmt.executeUpdate(query);
//                    System.out.println(query);
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
