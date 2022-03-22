package uk.co.automatictester.truststore.maven.plugin.keystore;

import uk.co.automatictester.truststore.maven.plugin.truststore.TruststoreFormat;

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
