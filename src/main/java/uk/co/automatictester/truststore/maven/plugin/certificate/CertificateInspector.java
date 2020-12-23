package uk.co.automatictester.truststore.maven.plugin.certificate;

import java.math.BigInteger;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class CertificateInspector {

    private final X509Certificate cert;

    public CertificateInspector(X509Certificate cert) {
        this.cert = cert;
    }

    public String getSerialNumber() {
        BigInteger serialNumber = cert.getSerialNumber();
        return bigIntToHexString(serialNumber);
    }

    public String getSubject() {
        return cert.getSubjectX500Principal().getName();
    }

    public String getIssuer() {
        return cert.getIssuerX500Principal().getName();
    }

    public String getNotValidBefore() {
        Date date = cert.getNotBefore();
        return formatDate(date);
    }

    public String getNotValidAfter() {
        Date date = cert.getNotAfter();
        return formatDate(date);
    }

    private String bigIntToHexString(BigInteger number) {
        String numberAsString = number.toString(16);
        if (numberAsString.length() % 2 == 1) {
            numberAsString = String.format("0%s", numberAsString);
        }
        return numberAsString.replaceAll("(?<=..)(..)", ":$1");
    }

    private String formatDate(Date date) {
        String pattern = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        return dateFormat.format(date);
    }
}
