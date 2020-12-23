package uk.co.automatictester.truststore.maven.plugin.certificate;

import org.testng.annotations.Test;

import java.security.cert.X509Certificate;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class CertificateReaderTest {

    @Test
    public void readDer() {
        List<X509Certificate> certs = CertificateReader.read("src/test/resources/cert/globalsign_root_ca_r2.der");
        X509Certificate cert = certs.get(0);
        String actualSerialNumber = cert.getSerialNumber().toString();
        String expectedSerialNumber = "4835703278459682885658125";
        assertThat(actualSerialNumber).isEqualTo(expectedSerialNumber);
    }

    @Test
    public void readPem() {
        List<X509Certificate> certs = CertificateReader.read("src/test/resources/cert/isrg_root_x1.pem");
        X509Certificate cert = certs.get(0);
        String actualSerialNumber = cert.getSerialNumber().toString();
        String expectedSerialNumber = "172886928669790476064670243504169061120";
        assertThat(actualSerialNumber).isEqualTo(expectedSerialNumber);
    }

    @Test
    public void readCertChainPem() {
        List<X509Certificate> certs = CertificateReader.read("src/test/resources/cert/www-google-com-chain.pem");
        assertThat(certs.size()).isEqualTo(3);

        List<SimpleEntry<Integer, String>> expectedCerts = new ArrayList<SimpleEntry<Integer, String>>() {{
            add(new SimpleEntry<>(0, "22730745842063135219043124570350083121"));
            add(new SimpleEntry<>(1, "149699596615803609916394524856"));
            add(new SimpleEntry<>(2, "4835703278459682885658125"));
        }};

        for (int i = 0; i < expectedCerts.size(); i++) {
            X509Certificate cert = certs.get(i);
            String actualSerialNumber = cert.getSerialNumber().toString();
            String expectedSerialNumber = expectedCerts.get(i).getValue();
            assertThat(actualSerialNumber).isEqualTo(expectedSerialNumber);
        }
    }

    @Test(expectedExceptions = RuntimeException.class, expectedExceptionsMessageRegExp = "Error reading file .*: .* \\(No such file or directory\\)")
    public void readNonexistent() {
        CertificateReader.read("src/test/resources/cert/nonexistent.der");
    }

    @Test(expectedExceptions = RuntimeException.class, expectedExceptionsMessageRegExp = "Error reading file .*: invalid DER-encoded certificate data")
    public void readCorrupted() {
        CertificateReader.read("src/test/resources/cert/corrupted.pem");
    }
}
