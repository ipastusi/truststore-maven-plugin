package uk.co.automatictester.truststore.maven.plugin.keystore;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import uk.co.automatictester.truststore.maven.plugin.truststore.TruststoreFormat;

import java.security.KeyStore;
import java.security.KeyStoreException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class KeyStoreFactory {

    public static KeyStore createInstance(TruststoreFormat format) throws KeyStoreException {
        switch (format) {
            case BCFKS:
                return KeyStore.getInstance(format.toString(), new BouncyCastleProvider());
            case JKS:
            case PKCS12:
                return KeyStore.getInstance(format.toString());
            default:
                throw new IllegalArgumentException("Unknown file format: " + format);
        }
    }
}
