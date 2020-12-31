package uk.co.automatictester.truststore.maven.plugin.certificate;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.math.BigInteger;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static uk.co.automatictester.truststore.maven.plugin.certificate.IncludeCertificates.*;

public class CertificateFilterTest {

    private final X509Certificate leafCert = mock(X509Certificate.class);
    private final X509Certificate caCert1 = mock(X509Certificate.class);
    private final X509Certificate caCert2 = mock(X509Certificate.class);

    private final List<X509Certificate> certChain = new ArrayList<X509Certificate>() {{
        add(leafCert);
        add(caCert1);
        add(caCert2);
    }};

    @BeforeClass
    public void setup() {
        when(leafCert.getSerialNumber()).thenReturn(new BigInteger("1"));
        when(caCert1.getSerialNumber()).thenReturn(new BigInteger("20"));
        when(caCert2.getSerialNumber()).thenReturn(new BigInteger("30"));
    }

    @Test
    public void testFilterAll() {
        CertificateFilter certFilter = new CertificateFilter(ALL);
        List<X509Certificate> filteredCerts = certFilter.filter(certChain);

        assertThat(filteredCerts.size()).isEqualTo(3);
    }

    @Test
    public void testFilterLeaf() {
        CertificateFilter certFilter = new CertificateFilter(LEAF);
        List<X509Certificate> filteredCerts = certFilter.filter(certChain);

        assertThat(filteredCerts.size()).isEqualTo(1);
        assertThat(filteredCerts.get(0).getSerialNumber()).isEqualTo("1");
    }

    @Test
    public void testFilterCa() {
        CertificateFilter certFilter = new CertificateFilter(CA);
        List<X509Certificate> filteredCerts = certFilter.filter(certChain);

        assertThat(filteredCerts.size()).isEqualTo(2);
        assertThat(filteredCerts.get(0).getSerialNumber()).isEqualTo("20");
        assertThat(filteredCerts.get(1).getSerialNumber()).isEqualTo("30");
    }

    @Test
    public void testFilterNull() {
        CertificateFilter certFilter = new CertificateFilter(ALL);
        List<X509Certificate> filteredCerts = certFilter.filter(null);
        assertThat(filteredCerts.size()).isEqualTo(0);
    }

    @Test
    public void testFilterEmpty() {
        CertificateFilter certFilter = new CertificateFilter(ALL);
        List<X509Certificate> filteredCerts = certFilter.filter(new ArrayList<>());
        assertThat(filteredCerts.size()).isEqualTo(0);
    }
}
