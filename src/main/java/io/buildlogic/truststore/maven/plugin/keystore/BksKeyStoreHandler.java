package io.buildlogic.truststore.maven.plugin.keystore;

import io.buildlogic.truststore.maven.plugin.truststore.TruststoreFormat;

public class BksKeyStoreHandler extends KeyStoreHandler {

    @Override
    public TruststoreFormat getFormat() {
        return TruststoreFormat.BKS;
    }

    @Override
    public void setNextHandler(KeyStoreHandler handler) {
        next = handler;
    }
}
