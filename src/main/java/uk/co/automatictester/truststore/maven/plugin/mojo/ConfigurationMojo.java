package uk.co.automatictester.truststore.maven.plugin.mojo;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.Parameter;
import uk.co.automatictester.truststore.maven.plugin.certificate.IncludeCertificates;
import uk.co.automatictester.truststore.maven.plugin.config.ScryptConfigSelector;
import uk.co.automatictester.truststore.maven.plugin.config.TruststoreSelector;
import uk.co.automatictester.truststore.maven.plugin.dns.DnsResolution;
import uk.co.automatictester.truststore.maven.plugin.truststore.TruststoreFormat;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
     * &lt;java.home&gt;/lib/security/jssecacerts or &lt;java.home&gt;/lib/security/cacerts
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
     * Timeout, in milliseconds, when downloading certificates.
     * Setting to 0 (zero) means no timeout.
     * Used as both connect and read timeout.
     * Default: 10000 (10s).
     */
    @Parameter(property = "truststore.downloadTimeout", defaultValue = "10000")
    protected int downloadTimeout;

    /**
     * Relevant only when specifying 'servers'.
     * DNS resolution option: SINGLE or ALL. Default: ALL.
     * Set to SINGLE to download certificates from a single IP address the hostname resolves to.
     * Set to ALL to download certificates from all IP addresses the hostname resolves to.
     * Relevant when DNS is configured to resolve given hostname to more than one IP address,
     * and different servers might be configured to use different X.509 certificates.
     */
    @Parameter(property = "truststore.dnsResolution", defaultValue = "ALL")
    protected DnsResolution dnsResolution;

    /**
     * Relevant only when specifying 'servers'.
     * List of custom DNS mappings. Optional.
     * If provided, and 'servers' include particular hostname,
     * specified IP will be used instead of default DNS resolution.
     * Otherwise DNS resolution strategy specified by 'dnsResolution' will be used.
     */
    @Parameter(property = "truststore.dnsMappings")
    private List<String> dnsMappings;

    protected Map<String, String> getDnsMappings() {
        Map<String, String> mappings = new HashMap<>();
        for (String mapping : dnsMappings) {
            int separator = mapping.indexOf(":");
            String host = mapping.substring(0, separator);
            String address = mapping.substring(separator + 1);
            mappings.put(host, address);
        }
        return mappings;
    }

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
