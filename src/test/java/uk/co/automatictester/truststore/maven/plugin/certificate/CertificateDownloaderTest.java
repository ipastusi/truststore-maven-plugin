package uk.co.automatictester.truststore.maven.plugin.certificate;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import uk.co.automatictester.truststore.maven.plugin.testutil.HttpsServer;

import java.security.cert.X509Certificate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class CertificateDownloaderTest {

    private HttpsServer server;

    @Test
    public void getHttpsServerCertificates() {
        server = new HttpsServer();
        int httpsPort = server.port();

        System.setProperty("javax.net.ssl.trustStore", "src/test/resources/truststore/wiremock_builtin_cert.p12");
        System.setProperty("javax.net.ssl.trustStorePassword", "changeit");

        String url = String.format("https://localhost:%d", httpsPort);
        CertificateDownloader certDownloader = new CertificateDownloader(false, true);
        List<X509Certificate> certs = certDownloader.getHttpsServerCertificates(url);
        assertThat(certs).hasSize(1);
        assertThat((certs.get(0)).getSerialNumber().toString()).isEqualTo("495529551");
    }

    @Test
    public void getHttpsServerCertificatesTrustAll() {
        server = new HttpsServer();
        int httpsPort = server.port();

        String url = String.format("https://localhost:%d", httpsPort);
        CertificateDownloader certDownloader = new CertificateDownloader(true, true);
        List<X509Certificate> certs = certDownloader.getHttpsServerCertificates(url);
        assertThat(certs).hasSize(1);
        assertThat((certs.get(0)).getSerialNumber().toString()).isEqualTo("495529551");
    }

    @Test(expectedExceptions = RuntimeException.class, expectedExceptionsMessageRegExp = ".*Connection refused.*")
    public void getHttpsServerCertificatesConnectionError() {
        server = new HttpsServer();
        int incorrectHttpsPort = server.port() - 1;

        String url = String.format("https://localhost:%d", incorrectHttpsPort);
        CertificateDownloader certDownloader = new CertificateDownloader(true, true);
        certDownloader.getHttpsServerCertificates(url);
    }

    @Test
    public void getHttpsServerCertificatesWithClientAuthAndTrustStore() {
        server = new HttpsServer(true);
        int httpsPort = server.port();

        System.setProperty("javax.net.ssl.keyStore", "src/test/resources/keystores/client_auth_key_cert.p12");
        System.setProperty("javax.net.ssl.keyStorePassword", "changeit");
        System.setProperty("javax.net.ssl.trustStore", "src/test/resources/truststore/wiremock_builtin_cert.p12");
        System.setProperty("javax.net.ssl.trustStorePassword", "changeit");

        String url = String.format("https://localhost:%d", httpsPort);
        CertificateDownloader certDownloader = new CertificateDownloader(false, true);
        List<X509Certificate> certs = certDownloader.getHttpsServerCertificates(url);
        assertThat(certs).hasSize(1);
        assertThat((certs.get(0)).getSerialNumber().toString()).isEqualTo("495529551");
    }

    @Test
    public void getHttpsServerCertificatesWithClientAuthAndTrustAll() {
        server = new HttpsServer(true);
        int httpsPort = server.port();

        System.setProperty("javax.net.ssl.keyStore", "src/test/resources/keystores/client_auth_key_cert.jks");
        System.setProperty("javax.net.ssl.keyStorePassword", "changeit");

        String url = String.format("https://localhost:%d", httpsPort);
        CertificateDownloader certDownloader = new CertificateDownloader(true, true);
        List<X509Certificate> certs = certDownloader.getHttpsServerCertificates(url);
        assertThat(certs).hasSize(1);
        assertThat((certs.get(0)).getSerialNumber().toString()).isEqualTo("495529551");
    }

    @Test(expectedExceptions = RuntimeException.class, expectedExceptionsMessageRegExp = ".* unable to find valid certification path to requested target")
    public void getHttpsServerCertificatesWithClientAuthNoKeyStore() {
        server = new HttpsServer(true);
        int httpsPort = server.port();

        String url = String.format("https://localhost:%d", httpsPort);
        CertificateDownloader certDownloader = new CertificateDownloader(false, true);
        certDownloader.getHttpsServerCertificates(url);
    }

    @Test
    public void getTlsServerCertificates() {
        CertificateDownloader certDownloader = new CertificateDownloader(false, true);
        List<X509Certificate> certs = certDownloader.getTlsServerCertificates("imap.gmail.com", 993);
        assertThat(certs).hasSizeGreaterThanOrEqualTo(1);
    }

    @AfterMethod
    public void stopServer() {
        System.clearProperty("javax.net.ssl.keyStore");
        System.clearProperty("javax.net.ssl.keyStorePassword");
        System.clearProperty("javax.net.ssl.trustStore");
        System.clearProperty("javax.net.ssl.trustStorePassword");

        if (server != null) {
            server.stop();
        }
    }
}
