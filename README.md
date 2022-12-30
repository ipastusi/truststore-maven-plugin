# Truststore Maven Plugin

[![Central status](https://maven-badges.herokuapp.com/maven-central/uk.co.automatictester/truststore-maven-plugin/badge.svg)](https://maven-badges.herokuapp.com/maven-central/uk.co.automatictester/truststore-maven-plugin)
[![Java](https://github.com/automatictester/truststore-maven-plugin/actions/workflows/maven.yml/badge.svg)](https://github.com/automatictester/truststore-maven-plugin/actions/workflows/maven.yml)
[![CodeQL](https://github.com/automatictester/truststore-maven-plugin/actions/workflows/codeql-analysis.yml/badge.svg)](https://github.com/automatictester/truststore-maven-plugin/actions/workflows/codeql-analysis.yml)

Maven plugin generating truststores in multiple formats.

## How it works

Truststore can be generated from:

- X.509 certificates sent by servers during TLS handshakes (including, but not limited to, HTTPS servers),
- or X.509 certificates stored on disk - both PEM and DER formats are supported,
- or X.509 certificates extracted from existing truststores stored on disk - truststores in BCFKS, BKS, JCEKS, JKS,
  PKCS12 and UBER formats are supported,
- or any combination of the above.

Sample execution log for the plugin configured to generate truststore only using leaf certificate downloaded from
`www.amazon.com:443`:

```
Downloading certificates through TLS handshake from server: www.amazon.com:443
Generating PKCS12 truststore
Serial number:       06:01:5d:8f:e3:6b:77:ab:df:66:b9:90:48:7e:da:40
Subject:             CN=www.amazon.com
Subject Alt Names:   amazon.com, amzn.com, uedata.amazon.com, us.amazon.com, www.amazon.com, www.amzn.com, corporate.amazon.com, buybox.amazon.com, iphone.amazon.com, yp.amazon.com, home.amazon.com, origin-www.amazon.com, buckeye-retail-website.amazon.com, huddles.amazon.com, p-nt-www-amazon-com-kalias.amazon.com, p-yo-www-amazon-com-kalias.amazon.com, p-y3-www-amazon-com-kalias.amazon.com
Issuer:              CN=DigiCert Global CA G2,O=DigiCert Inc,C=US
Valid between:       2021-04-19 00:00:00 and 2022-04-11 23:59:59 (GMT)

Total of 1 certificates saved to target/truststore.p12
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

            <!-- Output truststore format: BCFKS, BKS, JCEKS, JKS, PKCS12 and UBER. Default: PKCS12 -->
            <!-- User property: truststore.format -->
            <truststoreFormat>PKCS12</truststoreFormat>

            <!-- Truststore filename. Required -->
            <!-- User property: truststore.file -->
            <truststoreFile>target/truststore.p12</truststoreFile>

            <!-- Password for created truststore. Default: changeit -->
            <!-- User property: truststore.password -->
            <truststorePassword>changeit</truststorePassword>

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
            <!-- (in this order). Default: false. -->
            <!-- User property: truststore.includeDefaultTruststore -->
            <includeDefaultTruststore>true</includeDefaultTruststore>

            <!-- List of TLS servers to download the certificates from. Optional -->
            <!-- User property: truststore.servers -->
            <servers>
               <server>www.example.com:443</server>
               <server>www.another.com:443</server>
            </servers>

            <!-- Relevant only when specifying 'servers' -->
            <!-- Set to true to trust server certificate when downloading certificates. Default: false -->
            <!-- User property: truststore.trustAllCertificates -->
            <trustAllCertificates>true</trustAllCertificates>

            <!-- IN FUTURE RELEASE -->
            <!-- Relevant only when specifying 'servers' -->
            <!-- Set to false to disable retry on failure when downloading certificates. Default: true -->
            <!-- User property: truststore.retryDownloadOnFailure -->
            <retryDownloadOnFailure>false</retryDownloadOnFailure>

            <!-- IN FUTURE RELEASE -->
            <!-- Relevant only when specifying 'servers' -->
            <!-- Timeout, in milliseconds, when downloading certificates -->
            <!-- Used as both connect and read timeout -->
            <!-- Default: 0 (no timeout) -->
            <!-- User property: truststore.downloadTimeout -->
            <downloadTimeout>5000</downloadTimeout>

            <!-- IN FUTURE RELEASE -->
            <!-- Relevant only when specifying 'servers' -->
            <!-- DNS resolution options: SINGLE or ALL. Default: SINGLE -->
            <!-- Set to ALL to download certificates from all IP addresses the hostname resolves to -->
            <!-- Relevant when DNS is configured to resolve given hostname to more than one IP address, -->
            <!-- and different servers might be configured to use different X.509 certificates -->
            <dnsResolution>ALL</dnsResolution>

            <!-- Relevant only when specifying 'servers' -->
            <!-- Which certificates to download: ALL, LEAF, CA. Default: ALL. -->
            <!-- User property: truststore.includeCertificates -->
            <includeCertificates>LEAF</includeCertificates>

            <!-- Custom Scrypt config. Can be optionally specified when 'truststoreFormat' is set to BCFKS -->
            <!-- Ignored if specified for other types of truststores -->
            <!-- User property: truststore.scryptConfig -->
            <scryptConfig>
               <costParameter>2048</costParameter>
               <blockSize>16</blockSize>
               <parallelizationParameter>2</parallelizationParameter>
               <saltLength>20</saltLength>
            </scryptConfig>

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

When downloading certificates through TLS handshakes, standard Java PKI rules apply:

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

## Custom Scrypt config

There may be reasons for you to pass custom Scrypt config with a user property rather than define it
using `<scryptConfig>...</scryptConfig>` in `<configuration>...</configuration>` section as documented above. There is
no documented way to pass a list of complex objects to a Maven plugin as a user property. In order to be able to pass
them as a property, either on command line or using `<properties>...</properties>` section in your **pom.xml**, a custom
solution has been implemented. You can specify value of `truststore.scryptConfig` property using the following syntax:

```
costParameter=4096,blockSize=16,parallelizationParameter=2,saltLength=20
```

## Supported Java versions

Truststore Maven Plugin is tested against Java LTS versions 8, 11 and 17.
