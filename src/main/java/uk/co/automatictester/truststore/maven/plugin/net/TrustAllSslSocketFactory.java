package uk.co.automatictester.truststore.maven.plugin.net;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class TrustAllSslSocketFactory {

    private TrustAllSslSocketFactory() {
    }

    public static SSLSocketFactory createInstance() {
        KeyManager[] keyManager = null;
        TrustManager[] trustManagers = TrustAllTrustManagersFactory.createInstance();
        SSLContext sslContext;
        try {
            sslContext = SSLContext.getInstance("TLS");
            sslContext.init(keyManager, trustManagers, new SecureRandom());
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            throw new RuntimeException(e);
        }
        return sslContext.getSocketFactory();
    }
}
