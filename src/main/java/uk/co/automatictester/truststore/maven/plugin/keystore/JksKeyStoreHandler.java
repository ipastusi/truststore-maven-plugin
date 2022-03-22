package uk.co.automatictester.truststore.maven.plugin.keystore;

import uk.co.automatictester.truststore.maven.plugin.truststore.TruststoreFormat;

public class JksKeyStoreHandler extends KeyStoreHandler {

    @Override
    public TruststoreFormat getFormat() {
        return TruststoreFormat.JKS;
    }

    @Override
    public void setNextHandler(KeyStoreHandler handler) {
        next = handler;
    }
}
