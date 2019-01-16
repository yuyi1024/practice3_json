import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.TrustAllStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;

import java.io.Closeable;

public class HttpClientRequest {
    private String uri;

    public HttpClientRequest(String uri) throws Exception{
        this.uri = uri;
    }

    public String getUriContext() throws Exception{
        CloseableHttpClient httpClient = HttpClients
            .custom()
            .setSSLContext(new SSLContextBuilder().loadTrustMaterial(null, TrustAllStrategy.INSTANCE).build())
            .build();

        HttpGet httpGet = new HttpGet(uri);
        CloseableHttpResponse response = httpClient.execute(httpGet);

        HttpEntity httpEntity = response.getEntity();
        String uriContext = EntityUtils.toString(httpEntity);
        return uriContext;
    }
}

