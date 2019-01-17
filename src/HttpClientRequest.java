import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.TrustAllStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

public class HttpClientRequest {
    private String uri;
    private String uriContext;

    public HttpClientRequest(String uri){
        this.uri = uri;
    }

    public String getUriContext(){
        try {
            CloseableHttpClient httpClient = HttpClients
                .custom()
                .setSSLContext(new SSLContextBuilder().loadTrustMaterial(null, TrustAllStrategy.INSTANCE).build())
                .build();

            HttpGet httpGet = new HttpGet(uri);
            CloseableHttpResponse response = httpClient.execute(httpGet);

            HttpEntity httpEntity = response.getEntity();
            uriContext = EntityUtils.toString(httpEntity);


        } catch (Exception e) {
            e.printStackTrace();
            uriContext = "";
        } finally {
            return uriContext;
        }
    }
}

