package uk.co.automatictester.truststore.maven.plugin.mojo;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.Parameter;
import uk.co.automatictester.truststore.maven.plugin.certificate.IncludeCertificates;
import uk.co.automatictester.truststore.maven.plugin.config.ScryptConfigSelector;
import uk.co.automatictester.truststore.maven.plugin.config.TruststoreSelector;
import uk.co.automatictester.truststore.maven.plugin.truststore.TruststoreFormat;

import java.util.List;

abstract class ConfigurationMojo extends AbstractMojo {

    /**
     * Truststore format: JKS, JCEKS, PKCS12, BKS, UBER or BCFKS. Default: PKCS12.
     */
    @Parameter(property = "truststore.format", defaultValue = "PKCS12")
    protected TruststoreFormat truststoreFormat;

    /**
     * Custom Scrypt config. Can be optionally specified for use with BCFKS truststore.
     * Ignored if specified for other types of truststores.
     */
    @Parameter
    protected CustomScryptConfig scryptConfig;

    @Parameter(property = "truststore.scryptConfig")
    private String scryptConfigProperty;

    protected CustomScryptConfig getScryptConfig() {
        ScryptConfigSelector scryptConfigSelector = new ScryptConfigSelector();
        CustomScryptConfig selectedConfig = scryptConfigSelector.select(scryptConfig, scryptConfigProperty);
        if (selectedConfig != null) {
            selectedConfig.validate();
        }
        return selectedConfig;
    }

    /**
     * Truststore filename.
     */
    @Parameter(property = "truststore.file", required = true)
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
    protected boolean trustAllCertificates;

    /**
     * Set to true to skip hostname verification when downloading certificates.
     */
    @Parameter(property = "truststore.skipHostnameVerification", defaultValue = "false")
    protected boolean skipHostnameVerification;

    /**
     * Which certificates to download: ALL, LEAF, CA. Default: ALL.
     */
    @Parameter(property = "truststore.includeCertificates", defaultValue = "ALL")
    protected IncludeCertificates includeCertificates;

    /**
     * List of files with certificates to use.
     */
    @Parameter(property = "truststore.certificates")
    protected List<String> certificates;

    /**
     * List of HTTPS URLs to download the certificates from.
     * Deprecated, use servers instead.
     */
    @Deprecated
    @Parameter(property = "truststore.urls")
    protected List<String> urls;

    /**
     * List of servers to download the certificates from.
     */
    @Parameter(property = "truststore.servers")
    protected List<String> servers;

    /**
     * Set to true to skip plugin execution.
     */
    @Parameter(property = "truststore.skip", defaultValue = "false")
    protected boolean skip;

    /**
     * List of files with truststores to use.
     */
    @Parameter
    private List<Truststore> truststores;

    @Parameter(property = "truststore.truststores")
    private String truststoresProperty;

    protected List<Truststore> getTruststores() {
        TruststoreSelector truststoreSelector = new TruststoreSelector();
        return truststoreSelector.select(truststores, truststoresProperty);
    }

    /**
     * Set to true to load certificates from the default truststore
     * in either <java.home>/lib/security/jssecacerts or <java.home>/lib/security/cacerts
     * (in this order).
     */
    @Parameter(property = "truststore.includeDefaultTruststore", defaultValue = "false")
    protected boolean includeDefaultTruststore;
}
