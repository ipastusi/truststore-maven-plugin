package uk.co.automatictester.truststore.maven.plugin.mojo;

import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import uk.co.automatictester.truststore.maven.plugin.certificate.CertificateDownloader;
import uk.co.automatictester.truststore.maven.plugin.certificate.CertificateReader;
import uk.co.automatictester.truststore.maven.plugin.truststore.TruststoreReader;
import uk.co.automatictester.truststore.maven.plugin.truststore.TruststoreWriter;

import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

@Mojo(name = "generate-truststore", defaultPhase = LifecyclePhase.PRE_INTEGRATION_TEST)
public class TruststoreMojo extends ConfigurationMojo {

    private final List<X509Certificate> certs = new ArrayList<>();

    @Override
    public void execute() {
        loadFileSystemCerts();
        loadFileSystemTruststores();
        loadWebCerts();
        createTruststore();
    }

    private void loadFileSystemCerts() {
        for (String certificateFile : certificates) {
            List<X509Certificate> newCerts = CertificateReader.read(certificateFile);
            certs.addAll(newCerts);
        }
    }

    private void loadFileSystemTruststores() {
        if (truststores != null) {
            for (Truststore sourceTruststore : truststores) {
                String file = sourceTruststore.getFile();
                String password = sourceTruststore.getPassword();
                List<X509Certificate> newCerts = TruststoreReader.read(file, password);
                certs.addAll(newCerts);
            }
        }
    }

    private void loadWebCerts() {
        CertificateDownloader certDownloader = new CertificateDownloader(trustAllCerts, skipHostnameVerification);
        for (String url : urls) {
            List<X509Certificate> newCerts = certDownloader.getServerCertificates(url);
            certs.addAll(newCerts);
        }
    }

    private void createTruststore() {
        TruststoreWriter truststoreWriter = new TruststoreWriter(truststoreFormat, truststoreFile, truststorePassword);
        truststoreWriter.write(certs);
    }
}
