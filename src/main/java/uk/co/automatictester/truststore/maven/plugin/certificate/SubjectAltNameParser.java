package uk.co.automatictester.truststore.maven.plugin.certificate;

import lombok.RequiredArgsConstructor;
import org.apache.maven.plugin.logging.Log;

import java.security.cert.CertificateParsingException;
import java.security.cert.X509Certificate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class SubjectAltNameParser {

    private final Log log;

    public Optional<String> parse(X509Certificate cert) {
        try {
            Collection<List<?>> subjectAltNames = cert.getSubjectAlternativeNames();
            if (subjectAltNames != null) {
                StringBuilder stringBuilder = new StringBuilder();
                for (List<?> subjectAltName : subjectAltNames) {
                    stringBuilder.append(subjectAltName.get(1));
                    stringBuilder.append(", ");
                }
                int length = stringBuilder.length();
                stringBuilder.delete(length - 2, length);
                String subjectAltNameString = stringBuilder.toString();
                return Optional.of(subjectAltNameString);
            }
        } catch (CertificateParsingException e) {
            log.warn("Error reading subject alternative names");
        }
        return Optional.empty();
    }
}
