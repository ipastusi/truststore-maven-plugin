package uk.co.automatictester.truststore.maven.plugin.certificate;

import java.security.cert.X509Certificate;
import java.util.List;

public interface CertificateDownloader {
    List<X509Certificate> getTlsServerCertificates(String host, int port);
}
