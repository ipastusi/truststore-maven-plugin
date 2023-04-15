package uk.co.automatictester.truststore.maven.plugin.keystore;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import uk.co.automatictester.truststore.maven.plugin.bc.BouncyCastleKeyStore;
import uk.co.automatictester.truststore.maven.plugin.truststore.TruststoreFormat;

import java.security.KeyStore;
import java.security.KeyStoreException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class KeyStoreFactory {

    public static KeyStore createInstance(TruststoreFormat format) throws KeyStoreException {
        switch (format) {
            case BKS:
            case UBER:
            case BCFKS:
                return BouncyCastleKeyStore.getInstance(format.toString());
            case JKS:
            case JCEKS:
            case PKCS12:
                return KeyStore.getInstance(format.toString());
            default:
                throw new IllegalArgumentException("Unknown file format: " + format);
        }
    }
}
