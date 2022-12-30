package uk.co.automatictester.truststore.maven.plugin.certificate;

import org.apache.maven.plugin.logging.Log;

import java.security.cert.X509Certificate;
import java.util.List;
import java.util.function.Supplier;

public class RetryingCertificateDownloader implements CertificateDownloader {

    private final SimpleCertificateDownloader simpleCertificateDownloader;
    private final Log log;

    public RetryingCertificateDownloader(Log log, boolean trustAllCerts) {
        this.simpleCertificateDownloader = new SimpleCertificateDownloader(log, trustAllCerts);
        this.log = log;
    }

    @Override
    public List<X509Certificate> getTlsServerCertificates(String host, int port) {
        Supplier<List<X509Certificate>> getServerCertsAction = () -> simpleCertificateDownloader.getTlsServerCertificates(host, port);
        return executeWithRetry(getServerCertsAction);
    }

    private List<X509Certificate> executeWithRetry(Supplier<List<X509Certificate>> action) {
        try {
            return action.get();
        } catch (Exception e) {
            log.warn("Error: " + e + ", retrying...");
            return action.get();
        }
    }
}
