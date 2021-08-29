package uk.co.automatictester.truststore.maven.plugin.mojo;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import uk.co.automatictester.truststore.maven.plugin.certificate.CertificateDownloader;
import uk.co.automatictester.truststore.maven.plugin.certificate.CertificateFilter;
import uk.co.automatictester.truststore.maven.plugin.certificate.CertificateReader;
import uk.co.automatictester.truststore.maven.plugin.config.URLValidator;
import uk.co.automatictester.truststore.maven.plugin.file.FileChecker;
import uk.co.automatictester.truststore.maven.plugin.keystore.KeyStoreReader;
import uk.co.automatictester.truststore.maven.plugin.truststore.TruststoreWriter;

import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

@Mojo(name = "generate-truststore", defaultPhase = LifecyclePhase.PRE_INTEGRATION_TEST)
public class TruststoreMojo extends ConfigurationMojo {

    private final List<X509Certificate> certs = new ArrayList<>();

    @Override
    public void execute() throws MojoExecutionException {
        if (skip) {
            getLog().info("Requested to skip Truststore Maven Plugin execution");
            return;
        }

        loadFileSystemCerts();
        loadFileSystemTruststores();
        loadWebCerts();
        loadDefaultTruststore();
        createTruststore();
    }

    private void loadFileSystemCerts() {
        for (String certificateFile : certificates) {
            List<X509Certificate> newCerts = CertificateReader.read(certificateFile);
            certs.addAll(newCerts);
        }
    }

    private void loadFileSystemTruststores() {
        List<Truststore> truststores = getTruststores();
        for (Truststore sourceTruststore : truststores) {
            String file = sourceTruststore.getFile();
            String password = sourceTruststore.getPassword();
            readCertificates(file, password);
        }
    }

    private void loadWebCerts() {
        CertificateDownloader certDownloader = new CertificateDownloader(trustAllCertificates, skipHostnameVerification);
        URLValidator URLValidator = new URLValidator();
        CertificateFilter certFilter = new CertificateFilter(includeCertificates);
        for (String url : urls) {
            URLValidator.validate(url);
            List<X509Certificate> downloadedCerts = certDownloader.getServerCertificates(url);
            List<X509Certificate> filteredCerts = certFilter.filter(downloadedCerts);
            certs.addAll(filteredCerts);
        }
    }

    private void loadDefaultTruststore() throws MojoExecutionException {
        if (includeDefaultTruststore) {
            String javaHome = System.getProperty("java.home");
            String jssecacerts = String.format("%s/lib/security/jssecacerts", javaHome);
            String cacerts = String.format("%s/lib/security/cacerts", javaHome);
            String defaultPassword = "changeit";
            if (FileChecker.isReadableFile(jssecacerts)) {
                readCertificates(jssecacerts, defaultPassword);
            } else if (FileChecker.isReadableFile(cacerts)) {
                readCertificates(cacerts, defaultPassword);
            } else {
                throw new MojoExecutionException("Default truststore not found");
            }
        }
    }

    private void readCertificates(String file, String password) {
        getLog().info("Loading certificates from " + file);
        List<X509Certificate> newCerts = KeyStoreReader.readCertificates(file, password);
        certs.addAll(newCerts);
    }

    private void createTruststore() {
        TruststoreWriter truststoreWriter = new TruststoreWriter(truststoreFormat, truststoreFile, truststorePassword);
        truststoreWriter.write(certs);
    }
}
