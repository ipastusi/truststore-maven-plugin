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
        for (int i = 0; i < certs.length; i++) {
            Certificate cert = certs[i];
            logCertDetails(cert);
            String alias = String.format("cert_%d", i);
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

    private void logCertDetails(Certificate cert) {
        CertificateInspector certInspector = new CertificateInspector(cert);
        String message = String.format("Certificate Subject DN: %s", certInspector.getSubjectDN());
        System.out.println(message);
    }
}
