package uk.co.automatictester.truststore.maven.plugin.certificate;

import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugin.logging.SystemStreamLog;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.asn1.x509.GeneralNames;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import uk.co.automatictester.truststore.maven.plugin.testutil.TestCertificateGenerator;

import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class CertificateInspectorTest {

    private final Log log = new SystemStreamLog();
    private CertificateInspector certInspector;

    @BeforeClass
    public void setUp() throws Exception {
        X509Certificate cert = TestCertificateGenerator.generateV1();
        certInspector = new CertificateInspector(log, cert);
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
    public void testGetIssuer() {
        assertThat(certInspector.getIssuer()).isEqualTo("CN=My Name,O=My Org,L=My Location,C=My Country");
    }

    @Test
    public void testGetNotValidBefore() {
        assertThat(certInspector.getNotValidBefore()).isEqualTo("2021-01-01 00:00:00");
    }

    @Test
    public void testGetNotValidAfter() {
        assertThat(certInspector.getNotValidAfter()).isEqualTo("2025-01-01 00:00:00");
    }

    @Test
    public void testGetSubjectAlternativeNames() throws Exception {
        List<GeneralName> altNames = new ArrayList<GeneralName>() {{
            add(new GeneralName(GeneralName.dNSName, "*.example.com"));
            add(new GeneralName(GeneralName.dNSName, "example.com"));
        }};
        GeneralNames subjectAltNames = GeneralNames.getInstance(new DERSequence(altNames.toArray(new GeneralName[]{})));
        X509Certificate cert = TestCertificateGenerator.generateV3(subjectAltNames);
        certInspector = new CertificateInspector(log, cert);

        assertThat(certInspector.getSubjectAlternativeNames()).isEqualTo(Optional.of("*.example.com, example.com"));
    }
}
