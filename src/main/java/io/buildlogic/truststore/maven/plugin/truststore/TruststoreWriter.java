package io.buildlogic.truststore.maven.plugin.truststore;

import io.buildlogic.truststore.maven.plugin.certificate.CertificateInspector;
import io.buildlogic.truststore.maven.plugin.keystore.KeyStoreFactory;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.maven.plugin.logging.Log;
import io.buildlogic.truststore.maven.plugin.bc.BouncyCastleKeyStoreLoader;
import io.buildlogic.truststore.maven.plugin.mojo.CustomScryptConfig;

import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor
public class TruststoreWriter {

    private final Log log;
    private final TruststoreFormat format;
    private final String file;
    private final String password;

    @Setter
    private CustomScryptConfig scryptConfig;

    public void write(List<X509Certificate> certs) {
        if (certs.isEmpty()) {
            log.warn("Truststore not generated: no certificates to store");
            return;
        }
        Set<X509Certificate> deduplicatedCerts = deduplicateCerts(certs);
        KeyStore keyStore = populateKeyStore(deduplicatedCerts);
        saveKeyStore(keyStore);
    }

    private Set<X509Certificate> deduplicateCerts(List<X509Certificate> certs) {
        return new HashSet<>(certs);
    }

    private KeyStore populateKeyStore(Set<X509Certificate> certs) {
        KeyStore keyStore;
        try {
            keyStore = KeyStoreFactory.createInstance(format);
            keyStore = loadKeyStore(keyStore);
            for (X509Certificate cert : certs) {
                CertificateInspector certInspector = new CertificateInspector(log, cert);
                logCertDetails(certInspector);
                String issuer = certInspector.getIssuer();
                String serialNumber = certInspector.getSerialNumber();
                String alias = String.format("%s - %s", issuer, serialNumber);
                keyStore.setCertificateEntry(alias, cert);
            }
        } catch (IOException | CertificateException | NoSuchAlgorithmException | KeyStoreException e) {
            String cause = e.getMessage();
            String errorMessage = String.format("Error building truststore: %s", cause);
            throw new RuntimeException(errorMessage, e);
        }
        return keyStore;
    }

    private KeyStore loadKeyStore(KeyStore keyStore) throws CertificateException, IOException, NoSuchAlgorithmException {
        if (keyStore.getType().equals(TruststoreFormat.BCFKS.toString()) && scryptConfig != null) {
            log.info("Generating " + format + " truststore with custom Scrypt parameters:");
            log.info(scryptConfig.toString());
            return BouncyCastleKeyStoreLoader.load(keyStore, scryptConfig);
        } else {
            log.info("Generating " + format + " truststore");
            return KeyStoreLoader.load(keyStore);
        }
    }

    private void logCertDetails(CertificateInspector certInspector) {
        Optional<String> subjectAltNames = certInspector.getSubjectAlternativeNames();
        log.info("Serial number:     " + certInspector.getSerialNumber());
        log.info("Subject:           " + certInspector.getSubject());
        subjectAltNames.ifPresent(s -> log.info("Subject Alt Names: " + s));
        log.info("Issuer:            " + certInspector.getIssuer());
        log.info("Valid between:     " + certInspector.getNotValidBefore()
                + " and " + certInspector.getNotValidAfter() + " (GMT)" + System.lineSeparator());
    }

    private void saveKeyStore(KeyStore keyStore) {
        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            keyStore.store(outputStream, password.toCharArray());
            log.info("Total of " + keyStore.size() + " certificates saved to: " + file);
        } catch (IOException | CertificateException | NoSuchAlgorithmException | KeyStoreException e) {
            String cause = e.getMessage();
            String errorMessage = String.format("Error writing file %s: %s", file, cause);
            throw new RuntimeException(errorMessage, e);
        }
    }
}
