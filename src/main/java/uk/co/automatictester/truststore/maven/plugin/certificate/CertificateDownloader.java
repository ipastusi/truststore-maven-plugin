package uk.co.automatictester.truststore.maven.plugin.certificate;

import uk.co.automatictester.truststore.maven.plugin.net.HttpsURLConnectionFactory;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.List;

public class CertificateDownloader {

    private final boolean trustAllCerts;
    private final boolean skipHostnameVerification;

    public CertificateDownloader(boolean trustAllCerts, boolean skipHostnameVerification) {
        this.trustAllCerts = trustAllCerts;
        this.skipHostnameVerification = skipHostnameVerification;
    }

    public List<X509Certificate> getServerCertificates(String url) {
        HttpsURLConnection httpsUrlConnection = HttpsURLConnectionFactory.createInstance(url, trustAllCerts, skipHostnameVerification);
        X509Certificate[] certs;
        try {
            httpsUrlConnection.connect();
            certs = (X509Certificate[]) httpsUrlConnection.getServerCertificates();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            httpsUrlConnection.disconnect();
        }
        return Arrays.asList(certs);
    }
}
