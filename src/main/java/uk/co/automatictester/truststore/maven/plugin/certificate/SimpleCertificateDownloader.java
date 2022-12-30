package uk.co.automatictester.truststore.maven.plugin.certificate;

import org.apache.maven.plugin.logging.Log;
import uk.co.automatictester.truststore.maven.plugin.net.ConfigurableSSLSocketFactory;

import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.List;

public class SimpleCertificateDownloader implements CertificateDownloader {

    private final int timeout;
    private final Log log;
    private final SSLSocketFactory sslSocketFactory;

    public SimpleCertificateDownloader(Log log, boolean trustAllCerts, int timeout) {
        this.sslSocketFactory = ConfigurableSSLSocketFactory.createInstance(trustAllCerts);
        this.log = log;
        this.timeout = timeout;
    }

    @Override
    public List<X509Certificate> getTlsServerCertificates(String host, int port) {
        log.info("Downloading certificates through TLS handshake from server: " + host + ":" + port);
        X509Certificate[] certs;
        try {
            SSLSocket socket = (SSLSocket) sslSocketFactory.createSocket(host, port);
            socket.setSoTimeout(timeout);
            SSLSession session = socket.getSession();
            validateSslSession(session, host, port);
            certs = (X509Certificate[]) session.getPeerCertificates();
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return Arrays.asList(certs);
    }

    private void validateSslSession(SSLSession session, String host, int port) {
        if (!session.isValid()) {
            throw new RuntimeException("Unable to establish TLS connection with " + host + ":" + port);
        }
    }
}
