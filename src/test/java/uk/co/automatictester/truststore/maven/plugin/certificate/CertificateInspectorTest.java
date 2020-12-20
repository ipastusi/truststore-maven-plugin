package uk.co.automatictester.truststore.maven.plugin.certificate;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import uk.co.automatictester.truststore.maven.plugin.testutil.TestCertificateGenerator;

import java.security.cert.Certificate;

import static org.assertj.core.api.Assertions.assertThat;

public class CertificateInspectorTest {

    private CertificateInspector certInspector;

    @BeforeClass
    public void setUp() throws Exception {
        Certificate cert = TestCertificateGenerator.generate();
        certInspector = new CertificateInspector(cert);
    }

    @Test
    public void serialName() {
        assertThat(certInspector.getSerialNumber()).isEqualTo("01:e3:b4:9a");
    }

    @Test
    public void testGetSubject() {
        assertThat(certInspector.getSubject()).isEqualTo("CN=My Name,O=My Org,L=My Location,C=My Country");
    }

    @Test
    public void testGetNotValidBefore() {
        assertThat(certInspector.getNotValidBefore()).isEqualTo("2021-01-01 00:00:00");
    }

    @Test
    public void testGetNotValidAfter() {
        assertThat(certInspector.getNotValidAfter()).isEqualTo("2025-01-01 00:00:00");
    }
}
