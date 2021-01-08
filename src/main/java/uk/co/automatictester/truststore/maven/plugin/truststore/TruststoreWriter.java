package uk.co.automatictester.truststore.maven.plugin.truststore;

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
import java.util.Set;

public class TruststoreWriter {

    private final TruststoreFormat format;
    private final String file;
    private final String password;

    public TruststoreWriter(TruststoreFormat format, String file, String password) {
        this.format = format;
        this.file = file;
        this.password = password;
    }

    public void write(List<X509Certificate> certs) {
        if (certs.isEmpty()) {
            System.out.println("Truststore not generated: no certificates to store");
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
                CertificateInspector certInspector = new CertificateInspector(cert);
                logCertDetails(certInspector);
                String alias = certInspector.getSerialNumber();
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
        String details = String.format("%-18s %s\n%-18s %s\n%-18s %s\n%-18s %s and %s (GMT)\n",
                "Serial number: ", certInspector.getSerialNumber(),
                "Subject:", certInspector.getSubject(),
                "Issuer:", certInspector.getIssuer(),
                "Valid between: ", certInspector.getNotValidBefore(),
                certInspector.getNotValidAfter());
        System.out.println(details);
    }

    private void saveKeyStore(KeyStore keyStore) {
        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            keyStore.store(outputStream, password.toCharArray());
            String message = String.format("Total of %d certificates saved to %s", keyStore.size(), file);
            System.out.println(message);
        } catch (IOException | CertificateException | NoSuchAlgorithmException | KeyStoreException e) {
            String cause = e.getMessage();
            String errorMessage = String.format("Error writing file %s: %s", file, cause);
            throw new RuntimeException(errorMessage, e);
        }
    }
}
