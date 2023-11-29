package io.buildlogic.truststore.maven.plugin.config;

import lombok.RequiredArgsConstructor;
import io.buildlogic.truststore.maven.plugin.mojo.Truststore;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class TruststoreSelector {

    private final TruststoresPropertyParser truststoresPropertyParser;

    public TruststoreSelector() {
        this.truststoresPropertyParser = new TruststoresPropertyParser();
    }

    public List<Truststore> select(List<Truststore> truststores, String truststoresProperty) {
        if (truststores != null) {
            return truststores;
        } else if (truststoresProperty != null) {
            return truststoresPropertyParser.parse(truststoresProperty);
        } else {
            return new ArrayList<>();
        }
    }
}
