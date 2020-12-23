package uk.co.automatictester.truststore.maven.plugin.certificate;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

public class CertificateReader {

    public static List<X509Certificate> read(String file) {
        List<X509Certificate> certs = new ArrayList<>();
        File sourceFile = new File(file);
        try (InputStream inputStream = new FileInputStream(sourceFile)) {
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            certs.addAll((List<X509Certificate>) certificateFactory.generateCertificates(inputStream));
        } catch (IOException e) {
            throw new RuntimeException("Error reading file: " + file, e);
        } catch (CertificateException e) {
            throw new RuntimeException("Error reading certificate: " + file, e);
        }
        return certs;
    }
}
