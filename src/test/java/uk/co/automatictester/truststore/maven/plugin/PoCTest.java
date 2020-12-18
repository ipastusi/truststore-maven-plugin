package uk.co.automatictester.truststore.maven.plugin;

import org.testng.annotations.Test;

import java.security.cert.Certificate;
import java.util.Arrays;
import java.util.stream.Stream;

public class PoCTest {

    @Test
    public void test() throws Exception {
        CertificateDownloader certDownloader = new CertificateDownloader(true);
        Certificate[] certs = certDownloader.getServerCertificates("https://www.amazon.com");

        Certificate pemCert = CertificateReader.read("/isrg_root_x1.pem");
        Certificate derCert = CertificateReader.read("/globalsign_root_ca_r2.der");
        Certificate[] allCerts = Stream.concat(Arrays.stream(certs), Stream.of(pemCert, derCert))
                .toArray(Certificate[]::new);

        TruststoreFormat truststoreFormat = TruststoreFormat.JKS;
        String filename = "truststore";
        String password = "changeit";
        TruststoreWriter truststoreWriter = new TruststoreWriter(truststoreFormat, filename, password);
        truststoreWriter.write(allCerts);
    }
}
