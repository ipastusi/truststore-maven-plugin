package uk.co.automatictester.truststore.maven.plugin.mojo;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import uk.co.automatictester.truststore.maven.plugin.certificate.CertificateDownloader;
import uk.co.automatictester.truststore.maven.plugin.certificate.CertificateFilter;
import uk.co.automatictester.truststore.maven.plugin.certificate.CertificateReader;
import uk.co.automatictester.truststore.maven.plugin.config.URLValidator;
import uk.co.automatictester.truststore.maven.plugin.file.FileChecker;
import uk.co.automatictester.truststore.maven.plugin.keystore.KeyStoreReader;
import uk.co.automatictester.truststore.maven.plugin.truststore.TruststoreFormat;
import uk.co.automatictester.truststore.maven.plugin.truststore.TruststoreWriter;

import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

@Mojo(name = "generate-truststore", defaultPhase = LifecyclePhase.PRE_INTEGRATION_TEST)
public class TruststoreMojo extends ConfigurationMojo {

    private final CertificateDownloader certDownloader = new CertificateDownloader(trustAllCertificates, skipHostnameVerification);
    private final CertificateFilter certFilter = new CertificateFilter(includeCertificates);
    private final List<X509Certificate> certs = new ArrayList<>();

    @Override
    public void execute() throws MojoExecutionException {
        if (skip) {
            getLog().info("Requested to skip Truststore Maven Plugin execution");
            return;
        }

        validateScryptConfig();
        loadFileSystemCerts();
        loadFileSystemTruststores();
        loadHttpsCerts();
        loadTlsCerts();
        loadDefaultTruststore();
        createTruststore();
    }

    // fail fast on incorrect config
    private void validateScryptConfig() {
        if (scryptConfig != null) {
            scryptConfig.validate();
        }
    }

    private void loadFileSystemCerts() {
        for (String certificateFile : certificates) {
            getLog().info("Loading certificates from file: " + certificateFile);
            List<X509Certificate> newCerts = CertificateReader.read(certificateFile);
            certs.addAll(newCerts);
        }
    }

    private void loadFileSystemTruststores() {
        List<Truststore> truststores = getTruststores();
        for (Truststore sourceTruststore : truststores) {
            String file = sourceTruststore.getFile();
            String password = sourceTruststore.getPassword();
            getLog().info("Loading certificates from truststore: " + file);
            readCertificates(file, password);
        }
    }

    private void loadHttpsCerts() {
        URLValidator urlValidator = new URLValidator();
        for (String url : urls) {
            urlValidator.validate(url);
            getLog().info("Downloading certificates through TLS handshake from URL: " + url);
            List<X509Certificate> downloadedCerts = certDownloader.getHttpsServerCertificates(url);
            List<X509Certificate> filteredCerts = certFilter.filter(downloadedCerts);
            certs.addAll(filteredCerts);
        }
    }

    private void loadTlsCerts() {
        for (String hostPort : hostPorts) {
            int separator = hostPort.indexOf(":");
            String host = hostPort.substring(0, separator);
            int port = Integer.parseInt(hostPort.substring(separator + 1));
            getLog().info("Downloading certificates through TLS handshake from server: " + host + ":" + port);
            List<X509Certificate> downloadedCerts = certDownloader.getTlsServerCertificates(host, port);
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
                getLog().info("Loading certificates from default truststore: " + jssecacerts);
                readCertificates(jssecacerts, defaultPassword);
            } else if (FileChecker.isReadableFile(cacerts)) {
                getLog().info("Loading certificates from default truststore: " + cacerts);
                readCertificates(cacerts, defaultPassword);
            } else {
                throw new MojoExecutionException("Default truststore not found");
            }
        }
    }

    private void readCertificates(String file, String password) {
        List<X509Certificate> newCerts = KeyStoreReader.readCertificates(file, password);
        certs.addAll(newCerts);
    }

    private void createTruststore() {
        Log log = getLog();
        TruststoreWriter truststoreWriter = new TruststoreWriter(log, truststoreFormat, truststoreFile, truststorePassword);
        CustomScryptConfig scryptConfig = getScryptConfig();
        if (truststoreFormat.equals(TruststoreFormat.BCFKS) && scryptConfig != null) {
            truststoreWriter.setScryptConfig(scryptConfig);
        }
        truststoreWriter.write(certs);
    }
}
