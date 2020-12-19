package uk.co.automatictester.truststore.maven.plugin.certificate;

import java.io.File;
import java.io.FileInputStream;
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
            File sourceFile = new File(file);
            InputStream inputStream = new FileInputStream(sourceFile);
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            cert = (X509Certificate) certificateFactory.generateCertificate(inputStream);
            inputStream.close();
        } catch (IOException e) {
            throw new RuntimeException("Error reading file: " + file, e);
        } catch (CertificateException e) {
            throw new RuntimeException("Error reading certificate: " + file, e);
        }
        return cert;
    }
}
