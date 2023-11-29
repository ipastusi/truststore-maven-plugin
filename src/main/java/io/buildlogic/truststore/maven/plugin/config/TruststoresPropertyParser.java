package io.buildlogic.truststore.maven.plugin.config;

import io.buildlogic.truststore.maven.plugin.mojo.Truststore;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TruststoresPropertyParser {

    public List<Truststore> parse(String property) {
        List<Truststore> truststores = new ArrayList<>();

        Matcher truststoreMatcher = truststoreMatcher(property);
        while (truststoreMatcher.find()) {
            Matcher detailsMatcher = detailsMatcher(truststoreMatcher);
            if (detailsMatcher.find()) {
                Truststore truststore = extractTruststore(detailsMatcher);
                truststores.add(truststore);
            }
        }

        if (truststores.size() == 0) {
            throw new RuntimeException("Malformed property 'truststore.truststores': " + property);
        }

        return truststores;
    }

    private Matcher truststoreMatcher(String property) {
        String truststoreRegex = "\\[(file=.*?,password=.*?)]";
        Pattern truststorePattern = Pattern.compile(truststoreRegex);
        return truststorePattern.matcher(property);
    }

    private Matcher detailsMatcher(Matcher truststoreMatcher) {
        String detailsRegex = "file=(.*),password=(.*)";
        Pattern detailsPattern = Pattern.compile(detailsRegex);
        String extractedTruststore = truststoreMatcher.group(1);
        return detailsPattern.matcher(extractedTruststore);
    }

    private Truststore extractTruststore(Matcher detailsMatcher) {
        String file = detailsMatcher.group(1);
        String password = detailsMatcher.group(2);
        return new Truststore(file, password);
    }
}
