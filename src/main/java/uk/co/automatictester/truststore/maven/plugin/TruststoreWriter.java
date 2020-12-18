package uk.co.automatictester.truststore.maven.plugin;

import java.io.FileOutputStream;
import java.security.KeyStore;
import java.security.cert.Certificate;

public class TruststoreWriter {

    private final TruststoreFormat truststoreFormat;
    private final String filename;
    private final String password;

    public TruststoreWriter(TruststoreFormat truststoreFormat, String filename, String password) {
        this.truststoreFormat = truststoreFormat;
        this.filename = filename;
        this.password = password;
    }

    public void write(Certificate[] certs) throws Exception {
        String format = truststoreFormat.toString();
        KeyStore keyStore = KeyStore.getInstance(format);
        keyStore.load(null, null);
        for (Certificate cert : certs) {
            CertificateInspector certInspector = new CertificateInspector(cert);
            logCertDetails(certInspector);
            String alias = String.format("subject: '%s'", certInspector.getSubject());
            keyStore.setCertificateEntry(alias, cert);
        }
        String extension = truststoreFormat.extension();
        String file = String.format("%s.%s", filename, extension);
        FileOutputStream outputStream = new FileOutputStream(file);
        keyStore.store(outputStream, password.toCharArray());
        outputStream.close();
        String message = String.format("Total of %d certificates saved to %s", keyStore.size(), file);
        System.out.println(message);
    }

    private void logCertDetails(CertificateInspector certInspector) {
        String details = String.format("%-18s %s\n%-18s %s\n%-18s %s\n",
                "Subject:", certInspector.getSubject(),
                "Not valid before:", certInspector.getNotValidBefore(),
                "Not valid after:", certInspector.getNotValidAfter());
        System.out.println(details);
    }
}
