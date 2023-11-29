package io.buildlogic.truststore.maven.plugin.keystore;

import io.buildlogic.truststore.maven.plugin.truststore.TruststoreFormat;

public class BcfksKeyStoreHandler extends KeyStoreHandler {

    @Override
    public TruststoreFormat getFormat() {
        return TruststoreFormat.BCFKS;
    }

    @Override
    public void setNextHandler(KeyStoreHandler handler) {
        next = handler;
    }
}
