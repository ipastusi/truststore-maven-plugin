package uk.co.automatictester.truststore.maven.plugin.mojo;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.Parameter;
import uk.co.automatictester.truststore.maven.plugin.config.TruststoresPropertyParser;
import uk.co.automatictester.truststore.maven.plugin.truststore.TruststoreFormat;

import java.util.ArrayList;
import java.util.List;

abstract class ConfigurationMojo extends AbstractMojo {

    /**
     * Truststore format: JKS or PKCS12. Default: JKS.
     */
    @Parameter(property = "truststore.format", defaultValue = "JKS")
    protected TruststoreFormat truststoreFormat;

    /**
     * Truststore filename.
     */
    @Parameter(property = "truststore.file")
    protected String truststoreFile;

    /**
     * Password for created truststore. Default: changeit.
     */
    @Parameter(property = "truststore.password", defaultValue = "changeit")
    protected String truststorePassword;

    /**
     * Set to true to trust server certificate when downloading certificates.
     */
    @Parameter(property = "truststore.trustAllCertificates", defaultValue = "false")
    protected boolean trustAllCerts;

    /**
     * Set to true to skip hostname verification when downloading certificates.
     */
    @Parameter(property = "truststore.skipHostnameVerification", defaultValue = "false")
    protected boolean skipHostnameVerification;

    /**
     * List of files with certificates to use.
     */
    @Parameter(property = "truststore.certificates")
    protected List<String> certificates;

    /**
     * List of URLs to download the certificates from.
     */
    @Parameter(property = "truststore.urls")
    protected List<String> urls;

    /**
     * List of files with truststores to use.
     */
    @Parameter
    private List<Truststore> truststores;

    @Parameter(property = "truststore.truststores")
    private String truststoresProperty;

    protected List<Truststore> getTruststores() {
        if (truststores != null) {
            return truststores;
        } else if (truststoresProperty != null) {
            return TruststoresPropertyParser.parse(truststoresProperty);
        } else {
            return new ArrayList<>();
        }
    }
}
