package uk.co.automatictester.truststore.maven.plugin.certificate;

import org.testng.annotations.Test;

import java.security.cert.Certificate;
import java.security.cert.X509Certificate;

import static org.assertj.core.api.Assertions.assertThat;

public class CertificateReaderTest {

    @Test
    public void readPem() {
        Certificate cert = CertificateReader.read("/isrg_root_x1.pem");
        X509Certificate x509cert = (X509Certificate) cert;
        String actualSerialNumber = x509cert.getSerialNumber().toString();
        String expectedSerialNumber = "172886928669790476064670243504169061120";
        assertThat(actualSerialNumber).isEqualTo(expectedSerialNumber);
    }

    @Test
    public void readDer() {
        Certificate cert = CertificateReader.read("/globalsign_root_ca_r2.der");
        X509Certificate x509cert = (X509Certificate) cert;
        String actualSerialNumber = x509cert.getSerialNumber().toString();
        String expectedSerialNumber = "4835703278459682885658125";
        assertThat(actualSerialNumber).isEqualTo(expectedSerialNumber);
    }

    @Test(expectedExceptions = RuntimeException.class, expectedExceptionsMessageRegExp = "Error reading certificate")
    public void readCorrupted() {
        CertificateReader.read("/corrupted.der");
    }
}
