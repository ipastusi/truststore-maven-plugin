package uk.co.automatictester.truststore.maven.plugin.certificate;

import java.net.InetAddress;
import java.security.cert.X509Certificate;
import java.util.List;

public interface CertificateDownloader {
    List<X509Certificate> getTlsServerCertificates(InetAddress address, int port);
}
