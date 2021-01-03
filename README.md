# Truststore Maven Plugin

[![Build Status](https://travis-ci.com/automatictester/truststore-maven-plugin.svg?branch=master)](https://travis-ci.com/automatictester/truststore-maven-plugin)
[![Central status](https://maven-badges.herokuapp.com/maven-central/uk.co.automatictester/truststore-maven-plugin/badge.svg)](https://maven-badges.herokuapp.com/maven-central/uk.co.automatictester/truststore-maven-plugin)

Generate Java truststore as part of Maven lifecycle.

## How it works

This plugin generates Java truststore in either JKS or PKCS12 format. Truststore can be generated from:

- X.509 certificates downloaded at runtime from servers using provided URLs,
- or X.509 certificates stored on disk - both PEM and DER formats are supported,
- or X.509 certificates extracted from existing truststores stored on disk - truststores in both JKS and PKCS12 formats
  are supported,
- or any combination of the above.

Option to download certificates dynamically is particularly handy when the certificates expire frequently, or you don't
know the server(s) you need the certificates from until you run the Maven build.

Sample execution log for the plugin configured to generate truststore only using leaf certificate downloaded from
`https://www.amazon.com`:

```
Serial number:     05:f9:90:3b:91:2f:1a:00:9c:ad:c8:06:65:2b:da:18
Subject:           CN=www.amazon.com,O=Amazon.com\, Inc.,L=Seattle,ST=Washington,C=US
Issuer:            CN=DigiCert Global CA G2,O=DigiCert Inc,C=US
Valid between:     2020-07-13 00:00:00 and 2021-07-10 12:00:00 (GMT)

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

            <!-- Truststore format: JKS or PKCS12. Default: JKS -->
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

         </configuration>
      </execution>
   </executions>
</plugin>
```

## Note on using user properties

There is one caveat regarding the use of `truststore.truststores` property. There is no documented way to pass a list of
complex objects to a Maven plugin other than using `<configuration>...</configuration>` section. In order to be able to
pass them as a property, either on command line or using `<properties>...</properties>` section in your **pom.xml**, a
custom solution has been implemented. You can specify value of `truststore.truststores` property using the following
syntax:

```
[file=truststores/truststore-1.p12,password=changeit],[file=truststores/truststore-2.jks,password=topsecret]
```

## Supported Java versions

Truststore Maven Plugin is tested against a broad range of Java versions.
See [Travis CI config](https://github.com/automatictester/truststore-maven-plugin/blob/master/.travis.yml) for details.
