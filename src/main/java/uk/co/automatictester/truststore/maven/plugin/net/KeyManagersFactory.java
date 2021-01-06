package uk.co.automatictester.truststore.maven.plugin.net;

import uk.co.automatictester.truststore.maven.plugin.keystore.KeyStoreReader;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;

public class KeyManagersFactory {

    private KeyManagersFactory() {
    }

    public static KeyManager[] createInstance() {
        String algorithm = KeyManagerFactory.getDefaultAlgorithm();
        KeyManagerFactory keyManagerFactory;

        String keyStoreFile = System.getProperty("javax.net.ssl.keyStore", null);
        String keyStorePassword = System.getProperty("javax.net.ssl.keyStorePassword", null);

        try {
            KeyStore keyStore;
            keyManagerFactory = KeyManagerFactory.getInstance(algorithm);
            if (keyStoreFile != null && keyStorePassword != null) {
                keyStore = KeyStoreReader.readKeyStore(keyStoreFile, keyStorePassword);
                keyManagerFactory.init(keyStore, keyStorePassword.toCharArray());
            } else {
                keyManagerFactory.init(null, null);
            }
        } catch (NoSuchAlgorithmException | KeyStoreException | UnrecoverableKeyException e) {
            throw new RuntimeException(e);
        }

        return keyManagerFactory.getKeyManagers();
    }
}
