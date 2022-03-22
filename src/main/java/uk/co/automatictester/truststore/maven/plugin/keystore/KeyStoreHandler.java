package uk.co.automatictester.truststore.maven.plugin.keystore;

import uk.co.automatictester.truststore.maven.plugin.truststore.TruststoreFormat;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

public abstract class KeyStoreHandler {

    protected KeyStoreHandler next;

    public KeyStore handleRequest(String file, String password) {
        KeyStore keyStore;
        try (FileInputStream inputStream = new FileInputStream(file)) {
            TruststoreFormat format = getFormat();
            keyStore = KeyStoreFactory.createInstance(format);
            keyStore.load(inputStream, password.toCharArray());
        } catch (IOException | CertificateException | NoSuchAlgorithmException | KeyStoreException e) {
            if (e instanceof IOException && e.getMessage().equals("Invalid keystore format") && next != null) {
                return next.handleRequest(file, password);
            } else {
                String cause = e.getMessage();
                String errorMessage = String.format("Error reading file %s: %s", file, cause);
                throw new RuntimeException(errorMessage, e);
            }
        }
        return keyStore;
    }

    public abstract TruststoreFormat getFormat();

    public abstract void setNextHandler(KeyStoreHandler handler);
}
