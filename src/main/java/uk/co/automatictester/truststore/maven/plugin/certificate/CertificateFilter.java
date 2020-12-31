package uk.co.automatictester.truststore.maven.plugin.certificate;

import lombok.RequiredArgsConstructor;

import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class CertificateFilter {

    private final IncludeCertificates includeCerts;

    public List<X509Certificate> filter(List<X509Certificate> certs) {
        if (certs == null || certs.size() == 0) {
            return new ArrayList<>();
        }
        switch (includeCerts) {
            case ALL:
                return certs;
            case LEAF:
                return new ArrayList<X509Certificate>() {{
                    add(certs.get(0));
                }};
            case CA:
                List<X509Certificate> caCerts = new ArrayList<>();
                for (int i = 0; i < certs.size(); i++) {
                    if (i > 0) {
                        caCerts.add(certs.get(i));
                    }
                }
                return caCerts;
            default:
                throw new RuntimeException("Invalid certificate filtering option");
        }
    }
}
