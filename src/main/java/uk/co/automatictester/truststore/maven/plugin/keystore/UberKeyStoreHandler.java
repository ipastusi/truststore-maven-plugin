package uk.co.automatictester.truststore.maven.plugin.keystore;

import uk.co.automatictester.truststore.maven.plugin.truststore.TruststoreFormat;

public class UberKeyStoreHandler extends KeyStoreHandler {

    @Override
    public TruststoreFormat getFormat() {
        return TruststoreFormat.UBER;
    }

    @Override
    public void setNextHandler(KeyStoreHandler handler) {
        next = handler;
    }
}
