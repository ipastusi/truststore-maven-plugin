package uk.co.automatictester.truststore.maven.plugin.keystore;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.security.KeyStore;
import java.security.cert.X509Certificate;
import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class KeyStoreReaderTest {

    @DataProvider(name = "keyStoreFiles")
    public Object[][] keyStoreFiles() {
        return new String[][]{
                {"src/test/resources/keystores/client_auth_key_cert.jks"},
                {"src/test/resources/keystores/client_auth_key_cert.p12"},
        };
    }

    @Test(dataProvider = "keyStoreFiles")
    public void testReadKeyStore(String keyStoreFile) throws Exception {
        String password = "changeit";
        KeyStore keyStore = KeyStoreReader.readKeyStore(keyStoreFile, password);

        KeyStore.ProtectionParameter protectionParameter = new KeyStore.PasswordProtection(password.toCharArray());
        KeyStore.Entry entry = keyStore.getEntry("1", protectionParameter);
        String entryDetails = entry.toString();

        assertThat(entryDetails).contains("Private key entry and certificate chain with 1 elements");
        assertThat(entryDetails).contains("Subject: CN=truststore-maven-plugin-it");
    }

    @DataProvider(name = "trustStores")
    public Object[][] trustStores() {
        return new String[][]{
                {"src/test/resources/truststore/www_github_com.jks"},
                {"src/test/resources/truststore/www_github_com.jceks"},
                {"src/test/resources/truststore/www_github_com.p12"},
                {"src/test/resources/truststore/www_github_com.bks"},
                {"src/test/resources/truststore/www_github_com.uber"},
                {"src/test/resources/truststore/www_github_com.bcfks"},
                {"src/test/resources/truststore/www_github_com.bcsfks"},
        };
    }

    @Test(dataProvider = "trustStores")
    public void testReadCertificates(String trustStore) {
        List<X509Certificate> certs = KeyStoreReader.readCertificates(trustStore, "topsecret");

        Comparator<X509Certificate> comparator = Comparator.comparing(X509Certificate::getSerialNumber);
        certs.sort(comparator);

        assertThat(certs.size()).isEqualTo(2);
        assertThat(certs.get(0).getSerialNumber().toString()).isEqualTo("6489877074546166222510380951761917343");
        assertThat(certs.get(1).getSerialNumber().toString()).isEqualTo("7101927171473588541993819712332065657");
    }

    @Test
    public void testReadCertificatesPrivateKeyOnly() {
        List<X509Certificate> certs = KeyStoreReader.readCertificates("src/test/resources/truststore/private_key_only.p12", "changeit");
        assertThat(certs.size()).isEqualTo(0);
    }

    @Test(expectedExceptions = RuntimeException.class, expectedExceptionsMessageRegExp = "Error reading file .*")
    public void testReadCertificatesNonexistent() {
        KeyStoreReader.readCertificates("src/test/resources/truststore/nonexistent.p12", "changeit");
    }
}
