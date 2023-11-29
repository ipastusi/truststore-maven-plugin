package io.buildlogic.truststore.maven.plugin.keystore;

import io.buildlogic.truststore.maven.plugin.truststore.TruststoreFormat;

public class JksKeyStoreHandler extends KeyStoreHandler {

    @Override
    public TruststoreFormat getFormat() {
        // handles both JKS and PKCS12 truststores
        return TruststoreFormat.JKS;
    }

    @Override
    public void setNextHandler(KeyStoreHandler handler) {
        next = handler;
    }
}
