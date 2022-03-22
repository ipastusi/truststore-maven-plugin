package uk.co.automatictester.truststore.maven.plugin.keystore;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class KeyStoreReader {

    public static List<X509Certificate> readCertificates(String file, String password) {
        KeyStore keyStore = readKeyStore(file, password);
        return extractCertificates(keyStore);
    }

    public static KeyStore readKeyStore(String file, String password) {
        KeyStoreHandler jksKeyStoreHandler = new JksKeyStoreHandler();
        KeyStoreHandler bcfskHandler = new BcfksKeyStoreHandler();
        KeyStoreHandler jceksKeyStoreHandler = new JceksKeyStoreHandler();
        KeyStoreHandler uberKeyStoreHandler = new UberKeyStoreHandler();
        KeyStoreHandler bksKeyStoreHandler = new BksKeyStoreHandler();

        jksKeyStoreHandler.setNextHandler(bcfskHandler);
        bcfskHandler.setNextHandler(jceksKeyStoreHandler);
        jceksKeyStoreHandler.setNextHandler(uberKeyStoreHandler);
        uberKeyStoreHandler.setNextHandler(bksKeyStoreHandler);

        return jksKeyStoreHandler.handleRequest(file, password);
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
