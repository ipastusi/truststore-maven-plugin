package uk.co.automatictester.truststore.maven.plugin.certificate;

import org.apache.maven.plugin.logging.SystemStreamLog;
import org.testng.annotations.Test;

import java.security.cert.CertificateParsingException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SubjectAltNameParserTest {

    private final SubjectAltNameParser subjectAltNameParser = new SubjectAltNameParser(new SystemStreamLog());

    @Test
    public void testParseEmpty() throws CertificateParsingException {
        X509Certificate cert = mock(X509Certificate.class);
        when(cert.getSubjectAlternativeNames()).thenReturn(null);

        Optional<String> subjectAltNames = subjectAltNameParser.parse(cert);
        assertThat(subjectAltNames.isPresent()).isFalse();
    }

    @Test
    public void testParseSingleDnsName() throws CertificateParsingException {
        Collection<List<?>> collection = new ArrayList<List<?>>() {{
            add(new ArrayList<String>() {{
                add("2");
                add("apache.org");
            }});
        }};

        X509Certificate cert = mock(X509Certificate.class);
        when(cert.getSubjectAlternativeNames()).thenReturn(collection);

        Optional<String> subjectAltNames = subjectAltNameParser.parse(cert);
        assertThat(subjectAltNames.get()).isEqualTo("apache.org");
    }

    @Test
    public void testParseTwoDnsNames() throws CertificateParsingException {
        Collection<List<?>> collection = new ArrayList<List<?>>() {{
            add(new ArrayList<String>() {{
                add("2");
                add("*.example.com");
            }});
            add(new ArrayList<String>() {{
                add("2");
                add("example.com");
            }});
        }};

        X509Certificate cert = mock(X509Certificate.class);
        when(cert.getSubjectAlternativeNames()).thenReturn(collection);

        Optional<String> subjectAltNames = subjectAltNameParser.parse(cert);
        assertThat(subjectAltNames.get()).isEqualTo("*.example.com, example.com");
    }

    @Test
    public void testParseDnsNameAndIpAddress() throws CertificateParsingException {
        Collection<List<?>> collection = new ArrayList<List<?>>() {{
            add(new ArrayList<String>() {{
                add("2");
                add("localhost");
            }});
            add(new ArrayList<String>() {{
                add("7");
                add("127.0.0.1");
            }});
        }};

        X509Certificate cert = mock(X509Certificate.class);
        when(cert.getSubjectAlternativeNames()).thenReturn(collection);

        Optional<String> subjectAltNames = subjectAltNameParser.parse(cert);
        assertThat(subjectAltNames.get()).isEqualTo("localhost, 127.0.0.1");
    }
}
