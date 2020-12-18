package uk.co.automatictester.truststore.maven.plugin;

import java.io.IOException;
import java.io.InputStream;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

public class CertificateReader {

    public static Certificate read(String file) {
        X509Certificate cert;
        try {
            InputStream inputStream = CertificateReader.class.getResourceAsStream(file);
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            cert = (X509Certificate) certificateFactory.generateCertificate(inputStream);
            inputStream.close();
        } catch (IOException e) {
            throw new RuntimeException("Error reading file", e);
        } catch (CertificateException e) {
            throw new RuntimeException("Error reading certificate", e);
        }
        return cert;
    }
}
