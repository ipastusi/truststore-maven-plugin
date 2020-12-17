package uk.co.automatictester.truststore.maven.plugin;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.security.cert.Certificate;

public class CertificateDownloader {

    public Certificate[] getServerCertificates(String url, boolean trustAll) {
        HttpsURLConnection httpsUrlConnection = HttpsURLConnectionFactory.createInstance(url, trustAll);
        Certificate[] certs;
        try {
            httpsUrlConnection.connect();
            certs = httpsUrlConnection.getServerCertificates();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return certs;
    }
}
