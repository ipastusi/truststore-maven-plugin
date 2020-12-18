package uk.co.automatictester.truststore.maven.plugin;

import java.security.cert.Certificate;
import java.security.cert.X509Certificate;

public class CertificateInspector {

    private CertificateInspector() {
    }

    public static String getDetails(Certificate cert) {
        X509Certificate x509cert = (X509Certificate) cert;
        String subject = x509cert.getSubjectX500Principal().getName();
        String notBefore = x509cert.getNotBefore().toString();
        String notAfter = x509cert.getNotAfter().toString();
        return String.format("Subject: %s\nNot valid before: %s\nNot valid after: %s",
                subject, notBefore, notAfter);
    }
}
