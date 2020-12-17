package uk.co.automatictester.truststore.maven.plugin;

import org.testng.annotations.Test;

import java.security.cert.Certificate;

public class PoCTest {

    @Test
    public void test() throws Exception {
        CertificateDownloader certDownloader = new CertificateDownloader(true);
        Certificate[] certs = certDownloader.getServerCertificates("https://www.amazon.com");
        TruststoreFormat truststoreFormat = TruststoreFormat.JKS;
        String filename = "truststore";
        String password = "changeit";
        TruststoreWriter truststoreWriter = new TruststoreWriter(truststoreFormat, filename, password);
        truststoreWriter.write(certs);
    }
}
