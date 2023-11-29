package io.buildlogic.truststore.maven.plugin.bc;

import io.buildlogic.truststore.maven.plugin.mojo.CustomScryptConfig;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bouncycastle.crypto.util.PBKDFConfig;
import org.bouncycastle.crypto.util.ScryptConfig;
import org.bouncycastle.jcajce.BCFKSLoadStoreParameter;

import java.io.IOException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BouncyCastleKeyStoreLoader {

    public static KeyStore load(KeyStore keyStore, CustomScryptConfig scryptConfig)
            throws CertificateException, IOException, NoSuchAlgorithmException {
        PBKDFConfig pbkdfConfig = new ScryptConfig.Builder(
                scryptConfig.getCostParameter(),
                scryptConfig.getBlockSize(),
                scryptConfig.getParallelizationParameter())
                .withSaltLength(scryptConfig.getSaltLength())
                .build();
        BCFKSLoadStoreParameter bcfksLoadStoreParameter = new BCFKSLoadStoreParameter.Builder()
                .withStorePBKDFConfig(pbkdfConfig)
                .build();
        keyStore.load(bcfksLoadStoreParameter);
        return keyStore;
    }
}
