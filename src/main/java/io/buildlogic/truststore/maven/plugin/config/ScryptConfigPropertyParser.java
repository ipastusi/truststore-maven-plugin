package io.buildlogic.truststore.maven.plugin.config;

import io.buildlogic.truststore.maven.plugin.mojo.CustomScryptConfig;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ScryptConfigPropertyParser {

    public CustomScryptConfig parse(String property) {
        Matcher scryptConfigMatcher = scryptConfigMatcher(property);
        if (scryptConfigMatcher.find()) {
            Matcher detailsMatcher = detailsMatcher(scryptConfigMatcher);
            if (detailsMatcher.find()) {
                return extractScryptConfig(detailsMatcher);
            }
        }
        throw new RuntimeException("Malformed property 'truststore.scryptConfig': " + property);
    }

    private Matcher scryptConfigMatcher(String property) {
        String scryptConfigRegex = "(costParameter=\\d+?,blockSize=\\d+?,parallelizationParameter=\\d+?,saltLength=\\d+?)$";
        Pattern scryptConfigPattern = Pattern.compile(scryptConfigRegex);
        return scryptConfigPattern.matcher(property);
    }

    private Matcher detailsMatcher(Matcher truststoreMatcher) {
        String detailsRegex = "costParameter=(\\d+),blockSize=(\\d+),parallelizationParameter=(\\d+),saltLength=(\\d+)";
        Pattern detailsPattern = Pattern.compile(detailsRegex);
        String extractedScryptConfig = truststoreMatcher.group(1);
        return detailsPattern.matcher(extractedScryptConfig);
    }

    private CustomScryptConfig extractScryptConfig(Matcher detailsMatcher) {
        int costParameter = Integer.parseInt(detailsMatcher.group(1));
        int blockSize = Integer.parseInt(detailsMatcher.group(2));
        int parallelizationParameter = Integer.parseInt(detailsMatcher.group(3));
        int saltLength = Integer.parseInt(detailsMatcher.group(4));
        return new CustomScryptConfig(costParameter, blockSize, parallelizationParameter, saltLength);
    }
}
