package uk.co.automatictester.truststore.maven.plugin.certificate;

import uk.co.automatictester.truststore.maven.plugin.net.ConfigurableSSLSocketFactory;
import uk.co.automatictester.truststore.maven.plugin.net.HttpsURLConnectionFactory;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
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

    public List<X509Certificate> getHttpsServerCertificates(String url) {
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

    /**
     * Use for non-HTTPS servers only.
     */
    public List<X509Certificate> getTlsServerCertificates(String host, int port) {
        SSLSocketFactory factory = ConfigurableSSLSocketFactory.createInstance(trustAllCerts);
        X509Certificate[] certs;
        try {
            SSLSocket socket = (SSLSocket) factory.createSocket(host, port);
            SSLSession session = socket.getSession();
            socket.close();
            certs = (X509Certificate[]) session.getPeerCertificates();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return Arrays.asList(certs);
    }
}
