package io.buildlogic.truststore.maven.plugin.net;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class ConfigurableSSLSocketFactory {

    private ConfigurableSSLSocketFactory() {
    }

    public static SSLSocketFactory createInstance(boolean trustAllCerts) {
        KeyManager[] keyManagers = KeyManagersFactory.createInstance();
        TrustManager[] trustManagers = TrustManagersFactory.createInstance(trustAllCerts);
        SSLContext sslContext;
        try {
            sslContext = SSLContext.getInstance("TLS");
            sslContext.init(keyManagers, trustManagers, new SecureRandom());
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            throw new RuntimeException(e);
        }
        return sslContext.getSocketFactory();
    }
}
