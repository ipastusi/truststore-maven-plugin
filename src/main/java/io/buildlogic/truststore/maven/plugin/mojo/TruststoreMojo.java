package io.buildlogic.truststore.maven.plugin.mojo;

import io.buildlogic.truststore.maven.plugin.certificate.*;
import io.buildlogic.truststore.maven.plugin.keystore.KeyStoreReader;
import io.buildlogic.truststore.maven.plugin.truststore.TruststoreFormat;
import io.buildlogic.truststore.maven.plugin.truststore.TruststoreWriter;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import io.buildlogic.truststore.maven.plugin.bc.BouncyCastleKeyStore;
import io.buildlogic.truststore.maven.plugin.certificate.*;
import io.buildlogic.truststore.maven.plugin.dns.DnsResolver;
import io.buildlogic.truststore.maven.plugin.dns.DnsResolverFactory;
import io.buildlogic.truststore.maven.plugin.file.FileChecker;

import java.net.InetAddress;
import java.security.cert.X509Certificate;
import java.util.*;

@Mojo(name = "generate-truststore", defaultPhase = LifecyclePhase.PRE_INTEGRATION_TEST)
public class TruststoreMojo extends ConfigurationMojo {

    private final List<X509Certificate> certs = new ArrayList<>();

    @Override
    public void execute() throws MojoExecutionException {
        if (skip) {
            getLog().info("Requested to skip Truststore Maven Plugin execution");
            return;
        }

        validateConfig();
        loadFileSystemCerts();
        loadFileSystemTruststores();
        loadTlsCerts();
        loadDefaultTruststore();
        createTruststore();
    }

    // fail fast on incorrect config
    private void validateConfig() {
        validateDownloadTimeout();
        validateScryptConfig();
        validateProviderDependency();
    }

    private void validateDownloadTimeout() {
        if (downloadTimeout < 0) {
            throw new RuntimeException("downloadTimeout can not be negative");
        }
    }

    private void validateScryptConfig() {
        if (scryptConfig != null) {
            scryptConfig.validate();
        }
    }

    private void validateProviderDependency() {
        Set<TruststoreFormat> bouncycastleFormats = new HashSet<TruststoreFormat>() {{
            add(TruststoreFormat.BKS);
            add(TruststoreFormat.BCFKS);
            add(TruststoreFormat.UBER);
        }};
        if (bouncycastleFormats.contains(truststoreFormat)) {
            BouncyCastleKeyStore.getProvider();
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

    private void loadTlsCerts() {
        Map<String, String> dnsMappings = getDnsMappings();
        DnsResolver dnsResolver = DnsResolverFactory.getInstance(dnsResolution, dnsMappings);
        CertificateDownloader certDownloader = getCertDownloader();
        CertificateFilter certFilter = new CertificateFilter(includeCertificates);
        for (String server : servers) {
            int separator = server.indexOf(":");
            String host = server.substring(0, separator);
            int port = Integer.parseInt(server.substring(separator + 1));
            List<InetAddress> addresses = dnsResolver.resolve(host);
            List<X509Certificate> downloadedCerts = null;
            for (InetAddress address : addresses) {
                downloadedCerts = certDownloader.getTlsServerCertificates(address, port);
            }
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

    private CertificateDownloader getCertDownloader() {
        Log log = getLog();
        if (retryDownloadOnFailure) {
            return new RetryingCertificateDownloader(log, trustAllCertificates, downloadTimeout);
        } else {
            return new SimpleCertificateDownloader(log, trustAllCertificates, downloadTimeout);
        }
    }
}
