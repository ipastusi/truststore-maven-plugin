package uk.co.automatictester.truststore.maven.plugin.mojo;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.Parameter;
import uk.co.automatictester.truststore.maven.plugin.truststore.TruststoreFormat;

import java.io.File;
import java.net.URL;
import java.util.List;

abstract class ConfigurationMojo extends AbstractMojo {

    /**
     * Truststore format: JKS or PKCS12. Default: JKS.
     */
    @Parameter(property = "truststoreFormat", defaultValue = "JKS")
    protected TruststoreFormat truststoreFormat;

    /**
     * Truststore filename.
     */
    @Parameter(property = "truststoreFile")
    protected String truststoreFile;

    /**
     * Password for created truststore. Default: changeit.
     */
    @Parameter(property = "truststorePassword", defaultValue = "changeit")
    protected String truststorePassword;

    /**
     * Set to true to trust server certificate when downloading certificates.
     */
    @Parameter(property = "trustAllCertificates", defaultValue = "false")
    protected boolean trustAllCerts;

    /**
     * Set to true to skip hostname verification when downloading certificates.
     */
    @Parameter(property = "skipHostnameVerification", defaultValue = "false")
    protected boolean skipHostnameVerification;

    /**
     * List of files with certificates to use.
     */
    @Parameter(property = "certificates")
    protected List<String> certificates;

    /**
     * List of URLs to download the certificates from.
     */
    @Parameter(property = "urls")
    protected List<String> urls;
}
