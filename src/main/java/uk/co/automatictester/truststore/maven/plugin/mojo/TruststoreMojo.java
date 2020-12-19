package uk.co.automatictester.truststore.maven.plugin.mojo;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import uk.co.automatictester.truststore.maven.plugin.certificate.CertificateDownloader;
import uk.co.automatictester.truststore.maven.plugin.certificate.CertificateReader;
import uk.co.automatictester.truststore.maven.plugin.truststore.TruststoreWriter;

import java.security.cert.Certificate;
import java.util.ArrayList;
import java.util.List;

@Mojo(name = "generate-truststore", defaultPhase = LifecyclePhase.PRE_INTEGRATION_TEST)
public class TruststoreMojo extends ConfigurationMojo {

    private final List<Certificate> certs = new ArrayList<>();

    @Override
    public void execute() throws MojoExecutionException {
        loadFileSystemCerts();
        loadWebCerts();
        createTruststore();
    }

    private void loadFileSystemCerts() {
        for (String certificateFile : certificates) {
            Certificate newCert = CertificateReader.read(certificateFile);
            certs.add(newCert);
        }
    }

    private void loadWebCerts() {
        CertificateDownloader certDownloader = new CertificateDownloader(trustAllCerts, skipHostnameVerification);
        for (String url : urls) {
            List<Certificate> newCerts = certDownloader.getServerCertificates(url);
            certs.addAll(newCerts);
        }
    }

    private void createTruststore() throws MojoExecutionException {
        TruststoreWriter truststoreWriter = new TruststoreWriter(truststoreFormat, truststoreFile, truststorePassword);
        try {
            if (certs.isEmpty()) {
                getLog().info("Truststore not generated: no certificates to store");
            } else {
                truststoreWriter.write(certs);
            }
        } catch (Exception e) {
            throw new MojoExecutionException("Error writing truststore: ", e);
        }
    }
}
