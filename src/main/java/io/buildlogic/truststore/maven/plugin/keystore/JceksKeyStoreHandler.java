package io.buildlogic.truststore.maven.plugin.keystore;

import io.buildlogic.truststore.maven.plugin.truststore.TruststoreFormat;

public class JceksKeyStoreHandler extends KeyStoreHandler {

    @Override
    public TruststoreFormat getFormat() {
        return TruststoreFormat.JCEKS;
    }

    @Override
    public void setNextHandler(KeyStoreHandler handler) {
        next = handler;
    }
}
