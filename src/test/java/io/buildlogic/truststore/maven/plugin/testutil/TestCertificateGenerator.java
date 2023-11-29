package io.buildlogic.truststore.maven.plugin.testutil;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.GeneralNames;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v1CertificateBuilder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v1CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;

import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.cert.X509Certificate;
import java.security.spec.RSAKeyGenParameterSpec;
import java.util.Date;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TestCertificateGenerator {

    private static final X500Name name;
    private static final KeyPair keyPair;
    private static final Date validityDate;
    private static final BigInteger serialNumber;
    private static final String signatureAlgorithm;

    static {
        try {
            name = getX500Name();
            keyPair = generateRsaKeyPair();
            validityDate = new Date(1735689600000L);
            serialNumber = BigInteger.valueOf(31700122);
            signatureAlgorithm = "SHA256withRSAandMGF1";
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static KeyPair generateRsaKeyPair() throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        RSAKeyGenParameterSpec parameterSpec = new RSAKeyGenParameterSpec(2048, RSAKeyGenParameterSpec.F4);
        keyPairGenerator.initialize(parameterSpec);
        return keyPairGenerator.generateKeyPair();
    }

    private static X500Name getX500Name() {
        return new X500NameBuilder(BCStyle.INSTANCE)
                .addRDN(BCStyle.C, "My Country")
                .addRDN(BCStyle.L, "My Location")
                .addRDN(BCStyle.O, "My Org")
                .addRDN(BCStyle.CN, "My Name")
                .build();
    }

    public static X509Certificate generateV1() throws Exception {
        Date now = new Date(1609459200000L);

        X509v1CertificateBuilder certificateBuilder = new JcaX509v1CertificateBuilder(
                name, serialNumber, now, validityDate, name, keyPair.getPublic()
        );

        ContentSigner contentSigner = new JcaContentSignerBuilder(signatureAlgorithm)
                .setProvider(new BouncyCastleProvider())
                .build(keyPair.getPrivate());

        X509CertificateHolder certificateHolder = certificateBuilder.build(contentSigner);

        return new JcaX509CertificateConverter()
                .setProvider(new BouncyCastleProvider())
                .getCertificate(certificateHolder);
    }

    public static X509Certificate generateV3(GeneralNames subjectAltNames) throws Exception {
        Date now = new Date(1609459200000L);

        X509v3CertificateBuilder certificateBuilder = new JcaX509v3CertificateBuilder(
                name, serialNumber, now, validityDate, name, keyPair.getPublic()
        );

        boolean critical = false;
        certificateBuilder.addExtension(Extension.subjectAlternativeName, critical, subjectAltNames);

        ContentSigner contentSigner = new JcaContentSignerBuilder(signatureAlgorithm)
                .setProvider(new BouncyCastleProvider())
                .build(keyPair.getPrivate());

        X509CertificateHolder certificateHolder = certificateBuilder.build(contentSigner);

        return new JcaX509CertificateConverter()
                .setProvider(new BouncyCastleProvider())
                .getCertificate(certificateHolder);
    }
}
