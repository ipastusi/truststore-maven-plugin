# Truststore Maven Plugin

[![Central status](https://maven-badges.herokuapp.com/maven-central/uk.co.automatictester/truststore-maven-plugin/badge.svg)](https://maven-badges.herokuapp.com/maven-central/uk.co.automatictester/truststore-maven-plugin)

Maven plugin generating JKS, PKCS12 and BCFKS truststores.

## How it works

Truststore can be generated from:

- X.509 certificates sent by HTTPS servers during TLS handshakes,
- or X.509 certificates stored on disk - both PEM and DER formats are supported,
- or X.509 certificates extracted from existing truststores stored on disk - truststores in JKS, PKCS12 and BCFKS
  formats are supported,
- or any combination of the above.

Sample execution log for the plugin configured to generate truststore only using leaf certificate downloaded from
`https://www.amazon.com`:

```
Serial number:       06:01:5d:8f:e3:6b:77:ab:df:66:b9:90:48:7e:da:40
Subject:             CN=www.amazon.com
Subject Alt Names:   amazon.com, amzn.com, uedata.amazon.com, us.amazon.com, www.amazon.com, www.amzn.com, corporate.amazon.com, buybox.amazon.com, iphone.amazon.com, yp.amazon.com, home.amazon.com, origin-www.amazon.com, buckeye-retail-website.amazon.com, huddles.amazon.com, p-nt-www-amazon-com-kalias.amazon.com, p-yo-www-amazon-com-kalias.amazon.com, p-y3-www-amazon-com-kalias.amazon.com
Issuer:              CN=DigiCert Global CA G2,O=DigiCert Inc,C=US
Valid between:       2021-04-19 00:00:00 and 2022-04-11 23:59:59 (GMT)

Total of 1 certificates saved to target/truststore.jks
```

## Quick start guide

Add this plugin to your **pom.xml**:

```xml

<plugin>
   <groupId>uk.co.automatictester</groupId>
   <artifactId>truststore-maven-plugin</artifactId>
   <version><!-- see above badge for most recent version number --></version>
   <executions>
      <execution>
         <goals>
            <goal>generate-truststore</goal>
         </goals>

         <!-- Maven lifecycle phase when this plugin should run -->
         <phase>pre-integration-test</phase>

         <configuration>

            <!-- Truststore format: JKS, PKCS12 or BCFKS. Default: JKS -->
            <!-- User property: truststore.format -->
            <truststoreFormat>PKCS12</truststoreFormat>

            <!-- Truststore filename. Required -->
            <!-- User property: truststore.file -->
            <truststoreFile>target/truststore.p12</truststoreFile>

            <!-- Truststore password. Default: changeit -->
            <!-- User property: truststore.password -->
            <truststorePassword>topsecret</truststorePassword>

            <!-- List of files with certificates to use. Optional -->
            <!-- User property: truststore.certificates -->
            <certificates>
               <certificate>certs/cert-to-add.pem</certificate>
               <certificate>certs/cert-to-add.der</certificate>
            </certificates>

            <!-- List of files with source truststores to use. Optional -->
            <!-- User property: truststore.truststores -->
            <truststores>
               <truststore>
                  <file>truststores/truststore-1.p12</file>
                  <password>changeit</password>
               </truststore>
               <truststore>
                  <file>truststores/truststore-2.jks</file>
                  <password>topsecret</password>
               </truststore>
            </truststores>

            <!-- Set to true to load certificates from the default truststore in either -->
            <!-- <java.home>/lib/security/jssecacerts or <java.home>/lib/security/cacerts -->
            <!-- (in this order) -->
            <!-- User property: truststore.includeDefaultTruststore -->
            <includeDefaultTruststore>true</includeDefaultTruststore>

            <!-- List of URLs to download the certificates from. Optional -->
            <!-- User property: truststore.urls -->
            <urls>
               <url>https://www.example.com</url>
               <url>https://www.another.com</url>
            </urls>

            <!-- Relevant only when specifying 'urls' -->
            <!-- Set to true to trust server certificate when downloading certificates. Default: false -->
            <!-- User property: truststore.trustAllCertificates -->
            <trustAllCertificates>true</trustAllCertificates>

            <!-- Relevant only when specifying 'urls' -->
            <!-- Set to true to skip hostname verification when downloading certificates. Default: false -->
            <!-- User property: truststore.skipHostnameVerification -->
            <skipHostnameVerification>true</skipHostnameVerification>

            <!-- Relevant only when specifying 'urls' -->
            <!-- Which certificates to download: ALL, LEAF, CA. Default: ALL. -->
            <!-- User property: truststore.includeCertificates -->
            <includeCertificates>LEAF</includeCertificates>

            <!-- Set to true to skip plugin execution. Default: false -->
            <!-- User property: truststore.skip -->
            <skip>true</skip>

         </configuration>
      </execution>
   </executions>
</plugin>
```

## Downloading certificates

#### Default settings

When downloading certificates from servers, standard Java PKI rules apply:

- Default truststore is used
- Empty keystore is used

You might have already customised these settings with global `~/.mavenrc` or project-level `.mvn/jvm.config`.

#### Using custom truststore

You can override default truststore with `javax.net.ssl.trustStore` and `javax.net.ssl.trustStorePassword`. However, you
may prefer to set `trustAllCertificates` to `true` instead.

#### Using custom keystore

You can override default keystore with `javax.net.ssl.keyStore` and `javax.net.ssl.keyStorePassword`. This may be
required to download the certificates if the server is configured to require client authentication and terminate TLS
handshake if the client doesn't provide a certificate. Please note this is no longer the case with Java versions 11 and
above. These versions will let you download server certificates even if the server is configured to require client
authentication and client doesn't provide a certificate.

## Loading certificates from existing truststores

There may be reasons for you to pass a list of source truststores with a user property rather than define it
using `<truststores>...</truststores>` in `<configuration>...</configuration>` section as documented above. There is no
documented way to pass a list of complex objects to a Maven plugin as a user property. In order to be able to pass them
as a property, either on command line or using `<properties>...</properties>` section in your **pom.xml**, a custom
solution has been implemented. You can specify value of `truststore.truststores` property using the following syntax:

```
[file=truststores/truststore-1.p12,password=changeit],[file=truststores/truststore-2.jks,password=topsecret]
```

## Supported Java versions

Truststore Maven Plugin is tested against Java LTS versions 8, 11 and 17.
