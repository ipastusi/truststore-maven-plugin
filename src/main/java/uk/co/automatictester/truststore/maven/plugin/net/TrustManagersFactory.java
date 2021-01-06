package uk.co.automatictester.truststore.maven.plugin.net;

import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.cert.X509Certificate;

public class TrustManagersFactory {

    private TrustManagersFactory() {
    }

    public static TrustManager[] createInstance(boolean trustAllCerts) {
        TrustManager[] trustManagers;
        if (trustAllCerts) {
            trustManagers = new TrustManager[]{
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
        } else {
            trustManagers = null;
        }
        return trustManagers;
    }
}
