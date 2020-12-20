package uk.co.automatictester.truststore.maven.plugin.truststore;

import uk.co.automatictester.truststore.maven.plugin.certificate.CertificateInspector;

import java.io.FileOutputStream;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.util.List;

public class TruststoreWriter {

    private final TruststoreFormat format;
    private final String file;
    private final String password;

    public TruststoreWriter(TruststoreFormat format, String file, String password) {
        this.format = format;
        this.file = file;
        this.password = password;
    }

    public void write(List<Certificate> certs) throws Exception {
        if (certs.isEmpty()) {
            System.out.println("Truststore not generated: no certificates to store");
            return;
        }
        String format = this.format.toString();
        KeyStore keyStore = KeyStore.getInstance(format);
        keyStore.load(null, null);
        for (Certificate cert : certs) {
            CertificateInspector certInspector = new CertificateInspector(cert);
            logCertDetails(certInspector);
            String alias = certInspector.getSerialNumber();
            keyStore.setCertificateEntry(alias, cert);
        }
        FileOutputStream outputStream = new FileOutputStream(file);
        keyStore.store(outputStream, password.toCharArray());
        outputStream.close();
        String message = String.format("Total of %d certificates saved to %s", keyStore.size(), file);
        System.out.println(message);
    }

    private void logCertDetails(CertificateInspector certInspector) {
        String details = String.format("%-18s %s\n%-18s %s\n%-18s %s\n%-18s %s and %s (GMT)\n",
                "Serial number: ", certInspector.getSerialNumber(),
                "Subject:", certInspector.getSubject(),
                "Issuer:", certInspector.getIssuer(),
                "Valid between: ", certInspector.getNotValidBefore(),
                certInspector.getNotValidAfter());
        System.out.println(details);
    }
}
