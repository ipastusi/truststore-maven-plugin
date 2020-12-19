package uk.co.automatictester.truststore.maven.plugin.net;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

public class HttpsURLConnectionFactory {

    private HttpsURLConnectionFactory() {
    }

    public static HttpsURLConnection createInstance(String url, boolean trustAllCerts, boolean skipHostnameVerification) {
        URLConnection urlConnection;
        try {
            URL serverUrl = new URL(url);
            urlConnection = serverUrl.openConnection();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        HttpsURLConnection httpsUrlConnection = (HttpsURLConnection) urlConnection;
        if (trustAllCerts) {
            SSLSocketFactory sslSocketFactory = TrustAllSslSocketFactory.createInstance();
            httpsUrlConnection.setSSLSocketFactory(sslSocketFactory);
        }
        if (skipHostnameVerification) {
            httpsUrlConnection.setHostnameVerifier((hostname, sslSession) -> true);
        }
        return httpsUrlConnection;
    }
}
