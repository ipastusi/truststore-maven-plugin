package uk.co.automatictester.truststore.maven.plugin.truststore;

import org.assertj.core.util.Files;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import uk.co.automatictester.truststore.maven.plugin.testutil.TestCertificateGenerator;

import java.io.File;
import java.security.cert.Certificate;
import java.util.ArrayList;
import java.util.List;

import static uk.co.automatictester.truststore.maven.plugin.truststore.TruststoreFormat.JKS;
import static uk.co.automatictester.truststore.maven.plugin.truststore.TruststoreFormat.PKCS12;

public class TruststoreWriterTest {

    private String file;

    @DataProvider(name = "trustoreFormats")
    public Object[][] trustoreFormats() {
        return new TruststoreFormat[][]{
                {PKCS12},
                {JKS},
        };
    }

    @BeforeMethod
    public void setUp() {
        File temporaryFolder = Files.newTemporaryFolder();
        temporaryFolder.deleteOnExit();

        file = String.format("%s/test_truststore", temporaryFolder.getAbsolutePath());
        new File(file).deleteOnExit();
    }

    @Test(dataProvider = "trustoreFormats")
    public void write(TruststoreFormat format) throws Exception {
        Certificate cert = TestCertificateGenerator.generate();
        List<Certificate> certs = new ArrayList<Certificate>() {{
            add(cert);
        }};

        String password = "changeit";
        TruststoreWriter truststoreWriter = new TruststoreWriter(format, file, password);
        truststoreWriter.write(certs);
    }
}
