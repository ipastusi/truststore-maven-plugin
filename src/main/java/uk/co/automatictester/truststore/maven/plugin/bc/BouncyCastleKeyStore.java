package uk.co.automatictester.truststore.maven.plugin.bc;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.KeyStore;
import java.security.KeyStoreException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BouncyCastleKeyStore {

    public static KeyStore getInstance(String format) throws KeyStoreException {
        return KeyStore.getInstance(format, new BouncyCastleProvider());
    }
}
