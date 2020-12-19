package uk.co.automatictester.truststore.maven.plugin;

import org.testng.annotations.Test;
import uk.co.automatictester.truststore.maven.plugin.certificate.CertificateDownloader;
import uk.co.automatictester.truststore.maven.plugin.certificate.CertificateReader;
import uk.co.automatictester.truststore.maven.plugin.truststore.TruststoreFormat;
import uk.co.automatictester.truststore.maven.plugin.truststore.TruststoreWriter;

import java.security.cert.Certificate;
import java.util.Arrays;
import java.util.stream.Stream;

public class PoCTest {

    @Test(enabled = false)
    public void test() throws Exception {
        CertificateDownloader certDownloader = new CertificateDownloader(true, true);
        Certificate[] certs = certDownloader.getServerCertificates("https://www.amazon.com");

        Certificate pemCert = CertificateReader.read("/cert/isrg_root_x1.pem");
        Certificate derCert = CertificateReader.read("/cert/globalsign_root_ca_r2.der");
        Certificate[] allCerts = Stream.concat(Arrays.stream(certs), Stream.of(pemCert, derCert))
                .toArray(Certificate[]::new);

        TruststoreFormat format = TruststoreFormat.JKS;
        String file = "target/truststore.jks";
        String password = "password";
        TruststoreWriter truststoreWriter = new TruststoreWriter(format, file, password);
        truststoreWriter.write(allCerts);
    }
}
