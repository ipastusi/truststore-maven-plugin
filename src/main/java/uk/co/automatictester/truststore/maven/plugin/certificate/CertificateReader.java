package uk.co.automatictester.truststore.maven.plugin.certificate;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CertificateReader {

    public static List<X509Certificate> read(String file) {
        List<X509Certificate> certs = new ArrayList<>();
        File sourceFile = new File(file);
        try (InputStream inputStream = Files.newInputStream(sourceFile.toPath())) {
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            certs.addAll((List<X509Certificate>) certificateFactory.generateCertificates(inputStream));
        } catch (IOException | CertificateException e) {
            String cause = e.getMessage();
            String errorMessage = String.format("Error reading file %s: %s", file, cause);
            throw new RuntimeException(errorMessage, e);
        }
        return certs;
    }
}
