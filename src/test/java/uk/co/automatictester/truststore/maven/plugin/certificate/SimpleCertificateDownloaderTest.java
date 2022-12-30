package uk.co.automatictester.truststore.maven.plugin.certificate;

import org.apache.maven.monitor.logging.DefaultLog;
import org.apache.maven.plugin.logging.Log;
import org.codehaus.plexus.logging.console.ConsoleLogger;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import uk.co.automatictester.truststore.maven.plugin.testutil.HttpsServer;

import java.security.cert.X509Certificate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class SimpleCertificateDownloaderTest {

    private final Log log = new DefaultLog(new ConsoleLogger());
    private HttpsServer server;

    @Test
    public void getTlsServerCertificates() {
        server = new HttpsServer();
        int port = server.port();

        System.setProperty("javax.net.ssl.trustStore", "src/test/resources/truststore/wiremock_server_cert.p12");
        System.setProperty("javax.net.ssl.trustStorePassword", "changeit");

        CertificateDownloader certDownloader = new SimpleCertificateDownloader(log, false);
        List<X509Certificate> certs = certDownloader.getTlsServerCertificates("localhost", port);
        assertThat(certs).hasSize(1);
        assertThat((certs.get(0)).getSerialNumber().toString()).isEqualTo("285246514769703101131665982281179467186167826091");
    }

    @Test
    public void getTlsServerCertificatesTrustAll() {
        server = new HttpsServer();
        int port = server.port();

        CertificateDownloader certDownloader = new SimpleCertificateDownloader(log, true);
        List<X509Certificate> certs = certDownloader.getTlsServerCertificates("localhost", port);
        assertThat(certs).hasSize(1);
        assertThat((certs.get(0)).getSerialNumber().toString()).isEqualTo("285246514769703101131665982281179467186167826091");
    }

    @Test(expectedExceptions = RuntimeException.class, expectedExceptionsMessageRegExp = ".*Connection refused.*")
    public void getTlsServerCertificatesConnectionError() {
        server = new HttpsServer();
        int incorrectport = server.port() - 1;

        CertificateDownloader certDownloader = new SimpleCertificateDownloader(log, true);
        certDownloader.getTlsServerCertificates("localhost", incorrectport);
    }

    @Test
    public void getTlsServerCertificatesWithClientAuthAndTrustStore() {
        server = new HttpsServer(true);
        int port = server.port();

        System.setProperty("javax.net.ssl.keyStore", "src/test/resources/keystores/client_auth_key_cert.p12");
        System.setProperty("javax.net.ssl.keyStorePassword", "changeit");
        System.setProperty("javax.net.ssl.trustStore", "src/test/resources/truststore/wiremock_server_cert.p12");
        System.setProperty("javax.net.ssl.trustStorePassword", "changeit");

        CertificateDownloader certDownloader = new SimpleCertificateDownloader(log, false);
        List<X509Certificate> certs = certDownloader.getTlsServerCertificates("localhost", port);
        assertThat(certs).hasSize(1);
        assertThat((certs.get(0)).getSerialNumber().toString()).isEqualTo("285246514769703101131665982281179467186167826091");
    }

    @Test
    public void getTlsServerCertificatesWithClientAuthAndTrustAll() {
        server = new HttpsServer(true);
        int port = server.port();

        System.setProperty("javax.net.ssl.keyStore", "src/test/resources/keystores/client_auth_key_cert.jks");
        System.setProperty("javax.net.ssl.keyStorePassword", "changeit");

        CertificateDownloader certDownloader = new SimpleCertificateDownloader(log, true);
        List<X509Certificate> certs = certDownloader.getTlsServerCertificates("localhost", port);
        assertThat(certs).hasSize(1);
        assertThat((certs.get(0)).getSerialNumber().toString()).isEqualTo("285246514769703101131665982281179467186167826091");
    }

    @Test(expectedExceptions = RuntimeException.class, expectedExceptionsMessageRegExp = "Unable to establish TLS connection with localhost:.*")
    public void getTlsServerCertificatesWithClientAuthNoKeyStore() {
        server = new HttpsServer(true);
        int port = server.port();

        CertificateDownloader certDownloader = new SimpleCertificateDownloader(log, false);
        certDownloader.getTlsServerCertificates("localhost", port);
    }

    @Test
    public void getTlsServerCertificatesInvalidHostnameIgnoredAtAllTimes() {
        server = new HttpsServer();
        int port = server.port();

        CertificateDownloader certDownloader = new SimpleCertificateDownloader(log, true);
        List<X509Certificate> certs = certDownloader.getTlsServerCertificates("127.0.0.1", port);
        assertThat(certs).hasSize(1);
        assertThat((certs.get(0)).getSerialNumber().toString()).isEqualTo("285246514769703101131665982281179467186167826091");
    }

    @AfterMethod
    public void stopServer() {
        System.clearProperty("javax.net.ssl.keyStore");
        System.clearProperty("javax.net.ssl.keyStorePassword");
        System.clearProperty("javax.net.ssl.trustStore");
        System.clearProperty("javax.net.ssl.trustStorePassword");
    }
}
