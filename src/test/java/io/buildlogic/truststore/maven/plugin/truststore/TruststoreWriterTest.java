package io.buildlogic.truststore.maven.plugin.truststore;

import io.buildlogic.truststore.maven.plugin.testutil.TestCertificateGenerator;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugin.logging.SystemStreamLog;
import org.assertj.core.util.Files;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import io.buildlogic.truststore.maven.plugin.mojo.CustomScryptConfig;

import java.io.File;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import static io.buildlogic.truststore.maven.plugin.truststore.TruststoreFormat.*;


public class TruststoreWriterTest {

    private final Log log = new SystemStreamLog();
    private String file;

    @DataProvider(name = "truststoreFormats")
    public Object[][] truststoreFormats() {
        return new TruststoreFormat[][]{
                {JKS},
                {JCEKS},
                {PKCS12},
                {BKS},
                {UBER},
                {BCFKS},
        };
    }

    @BeforeMethod
    public void setUp() {
        File temporaryFolder = Files.newTemporaryFolder();
        temporaryFolder.deleteOnExit();

        file = String.format("%s/test_truststore", temporaryFolder.getAbsolutePath());
        new File(file).deleteOnExit();
    }

    @Test(dataProvider = "truststoreFormats")
    public void write(TruststoreFormat format) throws Exception {
        X509Certificate cert = TestCertificateGenerator.generateV1();
        List<X509Certificate> certs = new ArrayList<X509Certificate>() {{
            add(cert);
        }};

        String password = "changeit";
        TruststoreWriter truststoreWriter = new TruststoreWriter(log, format, file, password);
        truststoreWriter.write(certs);
    }

    @Test
    public void writeCustomScryptConfig() throws Exception {
        CustomScryptConfig config = new CustomScryptConfig(1024, 8, 1, 20);
        X509Certificate cert = TestCertificateGenerator.generateV1();
        List<X509Certificate> certs = new ArrayList<X509Certificate>() {{
            add(cert);
        }};

        String password = "changeit";
        TruststoreWriter truststoreWriter = new TruststoreWriter(log, BCFKS, file, password);
        truststoreWriter.setScryptConfig(config);
        truststoreWriter.write(certs);
    }

    @Test
    public void writeNoCerts() {
        List<X509Certificate> certs = new ArrayList<>();
        String password = "changeit";
        TruststoreWriter truststoreWriter = new TruststoreWriter(log, PKCS12, file, password);
        truststoreWriter.write(certs);
    }
}
