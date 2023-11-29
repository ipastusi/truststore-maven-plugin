package io.buildlogic.truststore.maven.plugin.certificate;

import org.apache.maven.plugin.logging.Log;
import io.buildlogic.truststore.maven.plugin.net.ConfigurableSSLSocketFactory;

import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
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
    public List<X509Certificate> getTlsServerCertificates(InetAddress address, int port) {
        log.info("Downloading certificates through TLS handshake from server: " + address + ":" + port);
        X509Certificate[] certs;
        try (SSLSocket socket = (SSLSocket) sslSocketFactory.createSocket()) {
            SocketAddress socketAddress = new InetSocketAddress(address, port);
            socket.setSoTimeout(timeout);
            socket.connect(socketAddress, timeout);
            SSLSession session = socket.getSession();
            certs = (X509Certificate[]) session.getPeerCertificates();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return Arrays.asList(certs);
    }
}
