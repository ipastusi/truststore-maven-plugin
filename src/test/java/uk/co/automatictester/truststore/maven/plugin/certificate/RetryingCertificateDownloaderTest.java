package uk.co.automatictester.truststore.maven.plugin.certificate;

import org.apache.maven.monitor.logging.DefaultLog;
import org.apache.maven.plugin.logging.Log;
import org.codehaus.plexus.logging.console.ConsoleLogger;
import org.testng.annotations.Test;
import uk.co.automatictester.truststore.maven.plugin.testutil.ChaosProxyServer;
import uk.co.automatictester.truststore.maven.plugin.testutil.ConnectionHandlingRules;
import uk.co.automatictester.truststore.maven.plugin.testutil.HttpsServer;

import java.security.cert.X509Certificate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.co.automatictester.truststore.maven.plugin.testutil.ConnectionHandlingRules.*;

public class RetryingCertificateDownloaderTest {

    private final Log log = new DefaultLog(new ConsoleLogger());
    private final HttpsServer server = new HttpsServer();
    private final int timeout = 1000;

    @Test
    public void getTlsServerCertificatesFirstSucceeded() throws InterruptedException {
        int targetPort = server.port();

        ConnectionHandlingRules[] connectionHandlingRules = new ConnectionHandlingRules[]{CONNECT};
        ChaosProxyServer proxyServer = new ChaosProxyServer(targetPort, connectionHandlingRules);
        int proxyPort = proxyServer.start();

        System.setProperty("javax.net.ssl.trustStore", "src/test/resources/truststore/wiremock_server_cert.p12");
        System.setProperty("javax.net.ssl.trustStorePassword", "changeit");

        CertificateDownloader certDownloader = new RetryingCertificateDownloader(log, false, timeout);
        List<X509Certificate> certs = certDownloader.getTlsServerCertificates("localhost", proxyPort);
        assertThat(certs).hasSize(1);
        assertThat((certs.get(0)).getSerialNumber().toString()).isEqualTo("285246514769703101131665982281179467186167826091");
    }

    @Test
    public void getTlsServerCertificatesFirstFailed() throws InterruptedException {
        int targetPort = server.port();

        ConnectionHandlingRules[] connectionHandlingRules = new ConnectionHandlingRules[]{DISCONNECT, CONNECT};
        ChaosProxyServer proxyServer = new ChaosProxyServer(targetPort, connectionHandlingRules);
        int proxyPort = proxyServer.start();

        System.setProperty("javax.net.ssl.trustStore", "src/test/resources/truststore/wiremock_server_cert.p12");
        System.setProperty("javax.net.ssl.trustStorePassword", "changeit");

        CertificateDownloader certDownloader = new RetryingCertificateDownloader(log, false, timeout);
        List<X509Certificate> certs = certDownloader.getTlsServerCertificates("localhost", proxyPort);
        assertThat(certs).hasSize(1);
        assertThat((certs.get(0)).getSerialNumber().toString()).isEqualTo("285246514769703101131665982281179467186167826091");
    }

    @Test
    public void getTlsServerCertificatesFirstTimedOut() throws InterruptedException {
        int targetPort = server.port();

        ConnectionHandlingRules[] connectionHandlingRules = new ConnectionHandlingRules[]{DELAY, CONNECT};
        ChaosProxyServer proxyServer = new ChaosProxyServer(targetPort, connectionHandlingRules);
        int proxyPort = proxyServer.start();

        System.setProperty("javax.net.ssl.trustStore", "src/test/resources/truststore/wiremock_server_cert.p12");
        System.setProperty("javax.net.ssl.trustStorePassword", "changeit");

        CertificateDownloader certDownloader = new RetryingCertificateDownloader(log, false, timeout);
        List<X509Certificate> certs = certDownloader.getTlsServerCertificates("localhost", proxyPort);
        assertThat(certs).hasSize(1);
        assertThat((certs.get(0)).getSerialNumber().toString()).isEqualTo("285246514769703101131665982281179467186167826091");
    }

    @Test(expectedExceptions = RuntimeException.class)
    public void getTlsServerCertificatesBothFailed() throws InterruptedException {
        int targetPort = server.port();
        ConnectionHandlingRules[] connectionHandlingRules = new ConnectionHandlingRules[]{DISCONNECT, DISCONNECT};
        ChaosProxyServer proxyServer = new ChaosProxyServer(targetPort, connectionHandlingRules);
        int proxyPort = proxyServer.start();

        System.setProperty("javax.net.ssl.trustStore", "src/test/resources/truststore/wiremock_server_cert.p12");
        System.setProperty("javax.net.ssl.trustStorePassword", "changeit");

        CertificateDownloader certDownloader = new RetryingCertificateDownloader(log, false, timeout);
        certDownloader.getTlsServerCertificates("localhost", proxyPort);
    }

    @Test(expectedExceptions = RuntimeException.class)
    public void getTlsServerCertificatesBothTimedOut() throws InterruptedException {
        int targetPort = server.port();
        ConnectionHandlingRules[] connectionHandlingRules = new ConnectionHandlingRules[]{DELAY, DELAY};
        ChaosProxyServer proxyServer = new ChaosProxyServer(targetPort, connectionHandlingRules);
        int proxyPort = proxyServer.start();

        System.setProperty("javax.net.ssl.trustStore", "src/test/resources/truststore/wiremock_server_cert.p12");
        System.setProperty("javax.net.ssl.trustStorePassword", "changeit");

        CertificateDownloader certDownloader = new RetryingCertificateDownloader(log, false, timeout);
        certDownloader.getTlsServerCertificates("localhost", proxyPort);
    }

    @Test
    public void getTlsServerCertificatesTimeoutDisabled() throws InterruptedException {
        int targetPort = server.port();
        ConnectionHandlingRules[] connectionHandlingRules = new ConnectionHandlingRules[]{DELAY, DELAY};
        ChaosProxyServer proxyServer = new ChaosProxyServer(targetPort, connectionHandlingRules);
        int proxyPort = proxyServer.start();
        int timeoutDisabled = 0;

        System.setProperty("javax.net.ssl.trustStore", "src/test/resources/truststore/wiremock_server_cert.p12");
        System.setProperty("javax.net.ssl.trustStorePassword", "changeit");

        CertificateDownloader certDownloader = new RetryingCertificateDownloader(log, false, timeoutDisabled);
        List<X509Certificate> certs = certDownloader.getTlsServerCertificates("localhost", proxyPort);
        assertThat(certs).hasSize(1);
        assertThat((certs.get(0)).getSerialNumber().toString()).isEqualTo("285246514769703101131665982281179467186167826091");
    }
}
