package uk.co.automatictester.truststore.maven.plugin.certificate;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.net.ServerSocket;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.List;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static org.assertj.core.api.Assertions.assertThat;

public class CertificateDownloaderTest {

    private WireMockServer server;

    @BeforeClass
    public void setUp() throws Exception {
        ServerSocket serverSocket = new ServerSocket(0);
        int httpsPort = serverSocket.getLocalPort();
        serverSocket.close();
        
        WireMockConfiguration options = options()
                .httpDisabled(true)
                .httpsPort(httpsPort);
        server = new WireMockServer(options);
        server.start();
    }

    @Test
    public void getServerCertificates() {
        int httpsPort = server.httpsPort();
        String url = String.format("https://localhost:%d", httpsPort);
        CertificateDownloader certDownloader = new CertificateDownloader(true, true);
        List<Certificate> certs = certDownloader.getServerCertificates(url);
        assertThat(certs).hasSize(1);
        assertThat(((X509Certificate) certs.get(0)).getSerialNumber().toString()).isEqualTo("495529551");
    }

    @Test(expectedExceptions = RuntimeException.class, expectedExceptionsMessageRegExp = ".*Connection refused.*")
    public void getServerCertificatesError() {
        int incorrectHttpsPort = server.httpsPort() - 1;
        String url = String.format("https://localhost:%d", incorrectHttpsPort);
        CertificateDownloader certDownloader = new CertificateDownloader(true, true);
        certDownloader.getServerCertificates(url);
    }

    @AfterClass
    public void tearDown() {
        server.stop();
    }
}
