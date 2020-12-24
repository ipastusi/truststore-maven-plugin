package uk.co.automatictester.truststore.maven.plugin.config;

public class URLValidator {

    public void validate(String url) {
        String validProtocol = "https://";
        if (!url.startsWith(validProtocol)) {
            String errorMessage = String.format("Invalid protocol in URL '%s'. URL must start with '%s'.", url, validProtocol);
            throw new RuntimeException(errorMessage);
        }
    }
}
