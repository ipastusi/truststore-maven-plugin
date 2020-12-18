package uk.co.automatictester.truststore.maven.plugin.certificate;

import uk.co.automatictester.truststore.maven.plugin.web.HttpsURLConnectionFactory;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.security.cert.Certificate;

public class CertificateDownloader {

    private final boolean trustAll;

    public CertificateDownloader(boolean trustAll) {
        this.trustAll = trustAll;
    }

    public Certificate[] getServerCertificates(String url) {
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
