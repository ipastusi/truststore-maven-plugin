package uk.co.automatictester.truststore.maven.plugin.web;

import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.cert.X509Certificate;

public class TrustAllTrustManagersFactory {

    private TrustAllTrustManagersFactory() {
    }

    public static TrustManager[] createInstance() {
        return new TrustManager[]{
                new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }

                    public void checkClientTrusted(X509Certificate[] certs, String authType) {
                    }

                    public void checkServerTrusted(X509Certificate[] certs, String authType) {
                    }
                }
        };
    }
}
