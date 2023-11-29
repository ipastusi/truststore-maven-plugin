package io.buildlogic.truststore.maven.plugin.bc;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.KeyStore;
import java.security.KeyStoreException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BouncyCastleKeyStore {

    public static KeyStore getInstance(String format) throws KeyStoreException {
        return KeyStore.getInstance(format, getProvider());
    }

    public static BouncyCastleProvider getProvider() {
        // this will throw an error if dependency is not available
        return new BouncyCastleProvider();
    }
}
