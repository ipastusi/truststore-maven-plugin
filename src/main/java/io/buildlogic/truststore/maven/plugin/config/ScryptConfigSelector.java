package io.buildlogic.truststore.maven.plugin.config;

import lombok.RequiredArgsConstructor;
import io.buildlogic.truststore.maven.plugin.mojo.CustomScryptConfig;

@RequiredArgsConstructor
public class ScryptConfigSelector {

    private final ScryptConfigPropertyParser scryptConfigPropertyParser;

    public ScryptConfigSelector() {
        this.scryptConfigPropertyParser = new ScryptConfigPropertyParser();
    }

    public CustomScryptConfig select(CustomScryptConfig scryptConfig, String scryptConfigProperty) {
        if (scryptConfig != null) {
            return scryptConfig;
        } else if (scryptConfigProperty != null) {
            return scryptConfigPropertyParser.parse(scryptConfigProperty);
        } else {
            return null;
        }
    }
}
