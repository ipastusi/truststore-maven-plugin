package uk.co.automatictester.truststore.maven.plugin.truststore;

import lombok.RequiredArgsConstructor;
import org.apache.maven.plugin.logging.Log;
import uk.co.automatictester.truststore.maven.plugin.certificate.CertificateInspector;

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
            keyStore = KeyStore.getInstance(format.toString());
            keyStore.load(null, null);
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
