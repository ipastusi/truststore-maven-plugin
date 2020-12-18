package uk.co.automatictester.truststore.maven.plugin.certificate;

import java.security.cert.Certificate;
import java.security.cert.X509Certificate;

public class CertificateInspector {

    private final Certificate cert;

    public CertificateInspector(Certificate cert) {
        this.cert = cert;
    }

    public String getSubject() {
        X509Certificate x509cert = (X509Certificate) cert;
        return x509cert.getSubjectX500Principal().getName();
    }

    public String getNotValidBefore() {
        X509Certificate x509cert = (X509Certificate) cert;
        return x509cert.getNotBefore().toString();
    }

    public String getNotValidAfter() {
        X509Certificate x509cert = (X509Certificate) cert;
        return x509cert.getNotAfter().toString();
    }
}
