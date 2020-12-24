package uk.co.automatictester.truststore.maven.plugin.truststore;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.security.cert.X509Certificate;
import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class TruststoreReaderTest {

    @DataProvider(name = "truststores")
    public Object[][] truststores() {
        return new String[][]{
                {"src/test/resources/truststore/www_github_com.jks"},
                {"src/test/resources/truststore/www_github_com.p12"},
        };
    }

    @Test(dataProvider = "truststores")
    public void testRead(String truststore) {
        List<X509Certificate> certs = TruststoreReader.read(truststore, "topsecret");

        Comparator<X509Certificate> comparator = Comparator.comparing(X509Certificate::getSerialNumber);
        certs.sort(comparator);

        assertThat(certs.size()).isEqualTo(2);
        assertThat(certs.get(0).getSerialNumber().toString()).isEqualTo("6489877074546166222510380951761917343");
        assertThat(certs.get(1).getSerialNumber().toString()).isEqualTo("7101927171473588541993819712332065657");
    }

    @Test
    public void testReadPrivateKeyOnly() {
        List<X509Certificate> certs = TruststoreReader.read("src/test/resources/truststore/private_key_only.p12", "changeit");
        assertThat(certs.size()).isEqualTo(0);
    }

    @Test(expectedExceptions = RuntimeException.class, expectedExceptionsMessageRegExp = "Error reading file .*: .* \\(No such file or directory\\)")
    public void readNonexistent() {
        TruststoreReader.read("src/test/resources/truststore/nonexistent.p12", "changeit");
    }
}
