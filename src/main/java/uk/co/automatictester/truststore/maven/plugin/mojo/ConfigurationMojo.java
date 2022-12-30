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
     * Output truststore format: JKS, JCEKS, PKCS12, BKS, UBER or BCFKS. Default: PKCS12.
     */
    @Parameter(property = "truststore.format", defaultValue = "PKCS12")
    protected TruststoreFormat truststoreFormat;

    /**
     * Truststore filename. Required.
     */
    @Parameter(property = "truststore.file", required = true)
    protected String truststoreFile;

    /**
     * Password for created truststore. Default: changeit.
     */
    @Parameter(property = "truststore.password", defaultValue = "changeit")
    protected String truststorePassword;

    /**
     * List of files with certificates to use. Optional.
     */
    @Parameter(property = "truststore.certificates")
    protected List<String> certificates;

    /**
     * List of files with source truststores to use. Optional.
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
     * Set to true to load certificates from the default truststore in either
     * <java.home>/lib/security/jssecacerts or <java.home>/lib/security/cacerts
     * (in this order). Default: false.
     */
    @Parameter(property = "truststore.includeDefaultTruststore", defaultValue = "false")
    protected boolean includeDefaultTruststore;

    /**
     * List of TLS servers to download the certificates from. Optional.
     */
    @Parameter(property = "truststore.servers")
    protected List<String> servers;

    /**
     * Relevant only when specifying 'servers'.
     * Set to true to trust server certificate when downloading certificates. Default: false.
     */
    @Parameter(property = "truststore.trustAllCertificates", defaultValue = "false")
    protected boolean trustAllCertificates;

    /**
     * Relevant only when specifying 'servers'.
     * Set to false to disable retry on failure when downloading certificates. Default: true.
     */
    @Parameter(property = "truststore.retryDownloadOnFailure", defaultValue = "true")
    protected boolean retryDownloadOnFailure;

    /**
     * Relevant only when specifying 'servers'.
     * Socket timeout, in milliseconds, when downloading certificates.
     * Default: 0 (no timeout).
     */
    @Parameter(property = "truststore.downloadTimeout", defaultValue = "0")
    protected int downloadTimeout;

    /**
     * Relevant only when specifying 'servers'.
     * Which certificates to download: ALL, LEAF, CA. Default: ALL.
     */
    @Parameter(property = "truststore.includeCertificates", defaultValue = "ALL")
    protected IncludeCertificates includeCertificates;

    /**
     * Custom Scrypt config. Can be optionally specified when 'truststoreFormat' is set to BCFKS.
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
     * Set to true to skip plugin execution. Default: false.
     */
    @Parameter(property = "truststore.skip", defaultValue = "false")
    protected boolean skip;
}
