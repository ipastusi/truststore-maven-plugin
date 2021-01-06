package uk.co.automatictester.truststore.maven.plugin.keystore;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class KeyStoreReader {

    private KeyStoreReader() {
    }

    public static List<X509Certificate> readCertificates(String file, String password) {
        KeyStore keyStore = readKeyStore(file, password);
        return extractCertificates(keyStore);
    }

    public static KeyStore readKeyStore(String file, String password) {
        KeyStore keyStore;
        try (FileInputStream inputStream = new FileInputStream(file)) {
            keyStore = KeyStore.getInstance("JKS");
            keyStore.load(inputStream, password.toCharArray());
        } catch (IOException | CertificateException | NoSuchAlgorithmException | KeyStoreException e) {
            String cause = e.getMessage();
            String errorMessage = String.format("Error reading file %s: %s", file, cause);
            throw new RuntimeException(errorMessage, e);
        }
        return keyStore;
    }

    private static List<X509Certificate> extractCertificates(KeyStore keyStore) {
        List<X509Certificate> certs;
        try {
            certs = new ArrayList<>();
            Enumeration<String> aliases = keyStore.aliases();
            while (aliases.hasMoreElements()) {
                String alias = aliases.nextElement();
                if (keyStore.isCertificateEntry(alias)) {
                    X509Certificate cert = (X509Certificate) keyStore.getCertificate(alias);
                    certs.add(cert);
                }
            }
        } catch (KeyStoreException e) {
            String cause = e.getMessage();
            String errorMessage = String.format("Error reading certificates: %s", cause);
            throw new RuntimeException(errorMessage, e);
        }
        return certs;
    }
}
