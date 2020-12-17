package uk.co.automatictester.truststore.maven.plugin;

import javax.security.auth.x500.X500Principal;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;

public class CertificateInspector {

    private final X509Certificate x509cert;

    public CertificateInspector(Certificate cert) {
        this.x509cert = (X509Certificate) cert;
    }

    public String getSubjectDN() {
        X500Principal subject = x509cert.getSubjectX500Principal();
        return subject.getName();
    }
}
