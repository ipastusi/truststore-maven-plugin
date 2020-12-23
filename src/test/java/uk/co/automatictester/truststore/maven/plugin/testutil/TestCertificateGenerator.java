package uk.co.automatictester.truststore.maven.plugin.testutil;

import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v1CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v1CertificateBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;

import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.cert.X509Certificate;
import java.security.spec.RSAKeyGenParameterSpec;
import java.util.Date;

public class TestCertificateGenerator {

    public static X509Certificate generate() throws Exception {
        X500Name name = getX500Name();
        KeyPair keyPair = generateRsaKeyPair();
        Date validityDate = new Date(1735689600000L);
        BigInteger serialNumber = BigInteger.valueOf(31700122);
        String signatureAlgorithm = "SHA256withRSAandMGF1";

        X509CertificateHolder certificateHolder = getCertificateHolder(
                name,
                keyPair,
                validityDate,
                serialNumber,
                signatureAlgorithm
        );

        return new JcaX509CertificateConverter()
                .setProvider(new BouncyCastleProvider())
                .getCertificate(certificateHolder);
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

    private static X509CertificateHolder getCertificateHolder(X500Name name, KeyPair keyPair, Date validityDate,
                                                              BigInteger serialNumber, String signatureAlgorithm)
            throws OperatorCreationException {
        Date now = new Date(1609459200000L);

        X509v1CertificateBuilder certificateBuilder = new JcaX509v1CertificateBuilder(
                name, serialNumber, now, validityDate, name, keyPair.getPublic()
        );

        ContentSigner contentSigner = new JcaContentSignerBuilder(signatureAlgorithm)
                .setProvider(new BouncyCastleProvider())
                .build(keyPair.getPrivate());

        return certificateBuilder.build(contentSigner);
    }
}
