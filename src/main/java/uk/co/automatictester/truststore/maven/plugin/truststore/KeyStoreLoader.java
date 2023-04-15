package uk.co.automatictester.truststore.maven.plugin.truststore;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class KeyStoreLoader {

    public static KeyStore load(KeyStore keyStore) throws CertificateException, IOException, NoSuchAlgorithmException {
        keyStore.load(null, null);
        return keyStore;
    }
}
