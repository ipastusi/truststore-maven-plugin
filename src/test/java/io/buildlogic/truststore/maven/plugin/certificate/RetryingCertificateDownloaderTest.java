package io.buildlogic.truststore.maven.plugin.certificate;

import org.apache.maven.monitor.logging.DefaultLog;
import org.apache.maven.plugin.logging.Log;
import org.codehaus.plexus.logging.console.ConsoleLogger;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;
import uk.co.automatictester.jproxy.JProxy;
import uk.co.automatictester.jproxy.ProxyConfig;
import uk.co.automatictester.jproxy.ProxyRuleList;
import io.buildlogic.truststore.maven.plugin.testutil.HttpsServer;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.cert.X509Certificate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class RetryingCertificateDownloaderTest {

    private final Log log = new DefaultLog(new ConsoleLogger());
    private final HttpsServer server = new HttpsServer();
    private final int timeout = 1000;
    private JProxy proxy;

    @AfterClass
    public void cleanup() throws InterruptedException {
        if (proxy != null) {
            proxy.stop();
        }
    }

    @Test
    public void getTlsServerCertificatesFirstSucceeded() throws InterruptedException, UnknownHostException {
        int targetPort = server.port();

        ProxyRuleList rules = new ProxyRuleList().addConnect();
        ProxyConfig config = ProxyConfig.builder()
                .targetPort(targetPort)
                .build();
        proxy = new JProxy(config, rules);
        int proxyPort = proxy.start();

        System.setProperty("javax.net.ssl.trustStore", "src/test/resources/truststore/wiremock_server_cert.p12");
        System.setProperty("javax.net.ssl.trustStorePassword", "changeit");

        CertificateDownloader certDownloader = new RetryingCertificateDownloader(log, false, timeout);
        InetAddress address = InetAddress.getByName("localhost");
        List<X509Certificate> certs = certDownloader.getTlsServerCertificates(address, proxyPort);
        assertThat(certs).hasSize(1);
        assertThat((certs.get(0)).getSerialNumber().toString()).isEqualTo("285246514769703101131665982281179467186167826091");
    }

    @Test
    public void getTlsServerCertificatesFirstFailed() throws InterruptedException, UnknownHostException {
        int targetPort = server.port();

        ProxyRuleList rules = new ProxyRuleList().addDisconnect().addConnect();
        ProxyConfig config = ProxyConfig.builder()
                .targetPort(targetPort)
                .build();
        proxy = new JProxy(config, rules);
        int proxyPort = proxy.start();

        System.setProperty("javax.net.ssl.trustStore", "src/test/resources/truststore/wiremock_server_cert.p12");
        System.setProperty("javax.net.ssl.trustStorePassword", "changeit");

        CertificateDownloader certDownloader = new RetryingCertificateDownloader(log, false, timeout);
        InetAddress address = InetAddress.getByName("localhost");
        List<X509Certificate> certs = certDownloader.getTlsServerCertificates(address, proxyPort);
        assertThat(certs).hasSize(1);
        assertThat((certs.get(0)).getSerialNumber().toString()).isEqualTo("285246514769703101131665982281179467186167826091");
    }

    @Test
    public void getTlsServerCertificatesFirstTimedOut() throws InterruptedException, UnknownHostException {
        int targetPort = server.port();

        ProxyRuleList rules = new ProxyRuleList().addDelayConnect(2000).addConnect();
        ProxyConfig config = ProxyConfig.builder()
                .targetPort(targetPort)
                .build();
        proxy = new JProxy(config, rules);
        int proxyPort = proxy.start();

        System.setProperty("javax.net.ssl.trustStore", "src/test/resources/truststore/wiremock_server_cert.p12");
        System.setProperty("javax.net.ssl.trustStorePassword", "changeit");

        CertificateDownloader certDownloader = new RetryingCertificateDownloader(log, false, timeout);
        InetAddress address = InetAddress.getByName("localhost");
        List<X509Certificate> certs = certDownloader.getTlsServerCertificates(address, proxyPort);
        assertThat(certs).hasSize(1);
        assertThat((certs.get(0)).getSerialNumber().toString()).isEqualTo("285246514769703101131665982281179467186167826091");
    }

    @Test(expectedExceptions = RuntimeException.class)
    public void getTlsServerCertificatesBothFailed() throws InterruptedException, UnknownHostException {
        int targetPort = server.port();

        ProxyRuleList rules = new ProxyRuleList().addDisconnect();
        ProxyConfig config = ProxyConfig.builder()
                .targetPort(targetPort)
                .build();
        proxy = new JProxy(config, rules);
        int proxyPort = proxy.start();

        System.setProperty("javax.net.ssl.trustStore", "src/test/resources/truststore/wiremock_server_cert.p12");
        System.setProperty("javax.net.ssl.trustStorePassword", "changeit");

        CertificateDownloader certDownloader = new RetryingCertificateDownloader(log, false, timeout);
        InetAddress address = InetAddress.getByName("localhost");
        certDownloader.getTlsServerCertificates(address, proxyPort);
    }

    @Test(expectedExceptions = RuntimeException.class)
    public void getTlsServerCertificatesBothTimedOut() throws InterruptedException, UnknownHostException {
        int targetPort = server.port();

        ProxyRuleList rules = new ProxyRuleList().addDelayDisconnect(2000);
        ProxyConfig config = ProxyConfig.builder()
                .targetPort(targetPort)
                .build();
        proxy = new JProxy(config, rules);
        int proxyPort = proxy.start();

        System.setProperty("javax.net.ssl.trustStore", "src/test/resources/truststore/wiremock_server_cert.p12");
        System.setProperty("javax.net.ssl.trustStorePassword", "changeit");

        CertificateDownloader certDownloader = new RetryingCertificateDownloader(log, false, timeout);
        InetAddress address = InetAddress.getByName("localhost");
        certDownloader.getTlsServerCertificates(address, proxyPort);
    }

    @Test
    public void getTlsServerCertificatesTimeoutDisabled() throws InterruptedException, UnknownHostException {
        int targetPort = server.port();

        ProxyRuleList rules = new ProxyRuleList().addDelayConnect(2000);
        ProxyConfig config = ProxyConfig.builder()
                .targetPort(targetPort)
                .build();
        proxy = new JProxy(config, rules);
        int proxyPort = proxy.start();
        int timeoutDisabled = 0;

        System.setProperty("javax.net.ssl.trustStore", "src/test/resources/truststore/wiremock_server_cert.p12");
        System.setProperty("javax.net.ssl.trustStorePassword", "changeit");

        CertificateDownloader certDownloader = new RetryingCertificateDownloader(log, false, timeoutDisabled);
        InetAddress address = InetAddress.getByName("localhost");
        List<X509Certificate> certs = certDownloader.getTlsServerCertificates(address, proxyPort);
        assertThat(certs).hasSize(1);
        assertThat((certs.get(0)).getSerialNumber().toString()).isEqualTo("285246514769703101131665982281179467186167826091");
    }
}
