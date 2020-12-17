package uk.co.automatictester.truststore.maven.plugin;

public enum TruststoreFormat {
    JKS("jks"),
    PKCS12("p12");

    private final String extension;

    TruststoreFormat(String extension) {
        this.extension = extension;
    }

    public String extension() {
        return this.extension;
    }
}
