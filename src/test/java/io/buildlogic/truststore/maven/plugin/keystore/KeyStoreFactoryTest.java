package io.buildlogic.truststore.maven.plugin.keystore;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import io.buildlogic.truststore.maven.plugin.truststore.TruststoreFormat;

import java.security.KeyStore;
import java.security.KeyStoreException;

import static org.assertj.core.api.Assertions.assertThat;
import static io.buildlogic.truststore.maven.plugin.truststore.TruststoreFormat.*;

public class KeyStoreFactoryTest {

    @DataProvider(name = "truststoreFormats")
    public Object[][] truststoreFormats() {
        return new Object[][]{
                {JKS, null},
                {JCEKS, null},
                {PKCS12, null},
                {BKS, "BC"},
                {UBER, "BC"},
                {BCFKS, "BC"},
        };
    }

    @Test(dataProvider = "truststoreFormats")
    public void testCreateInstance(TruststoreFormat format, String provider) throws KeyStoreException {
        KeyStore keyStore = KeyStoreFactory.createInstance(format);
        assertThat(keyStore.getType()).isEqualTo(format.toString());
        if (provider != null) {
            assertThat(keyStore.getProvider().getName()).isEqualTo(provider);
        }
    }
}
