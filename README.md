# Truststore Maven Plugin

[![Build Status](https://travis-ci.com/automatictester/truststore-maven-plugin.svg?branch=master)](https://travis-ci.com/automatictester/truststore-maven-plugin)
[![Central status](https://maven-badges.herokuapp.com/maven-central/uk.co.automatictester/truststore-maven-plugin/badge.svg)](https://maven-badges.herokuapp.com/maven-central/uk.co.automatictester/truststore-maven-plugin)

Generate Java truststore as part of Maven lifecycle.

## How it works

This plugin generates Java truststore in either JKS or PKCS12 format. Truststore can be generated from:

- X.509 certificates stored on disk - both PEM and DER formats are supported,
- or X.509 certificates downloaded at runtime from servers using provided URLs,
- or both.

Option to download certificates dynamically is particularly handy when the server(s) certificates expire frequently, or
you don't know the server(s) you need the certificates from until you run the Maven build.

Sample execution log for the plugin configured to generate truststore only using certificates downloaded from
`https://www.amazon.com`:

```
Serial number:     05:f9:90:3b:91:2f:1a:00:9c:ad:c8:06:65:2b:da:18
Subject:           CN=www.amazon.com,O=Amazon.com\, Inc.,L=Seattle,ST=Washington,C=US
Not valid before:  2020-07-13 00:00:00
Not valid after:   2021-07-10 12:00:00

Serial number:     0c:8e:e0:c9:0d:6a:89:15:88:04:06:1e:e2:41:f9:af
Subject:           CN=DigiCert Global CA G2,O=DigiCert Inc,C=US
Not valid before:  2013-08-01 12:00:00
Not valid after:   2028-08-01 12:00:00

Serial number:     63:18:0d:38:fb:80:97:78:a9:d0:35:a3:16:18:f8:40
Subject:           CN=DigiCert Global Root G2,OU=www.digicert.com,O=DigiCert Inc,C=US
Not valid before:  2017-11-06 00:00:00
Not valid after:   2022-11-05 23:59:59

Total of 3 certificates saved to target/truststore.jks
```

## Quick start guide

- Add this plugin to your **pom.xml**:

```
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
            <truststoreFormat>PKCS12</truststoreFormat>
            
            <!-- Truststore filename -->
            <truststoreFile>target/truststore.p12</truststoreFile>
            
            <!-- Password for created truststore. Default: changeit -->
            <truststorePassword>topsecret</truststorePassword>
            
            <!-- Relevant only when specifying 'urls' -->
            <!-- Set to true to trust server certificate when downloading certificates. Default: false -->
            <trustAllCertificates>true</trustAllCertificates>
            
            <!-- Relevant only when specifying 'urls' -->
            <!-- Set to true to skip hostname verification when downloading certificates. Default: false -->
            <skipHostnameVerification>true</skipHostnameVerification>
            
            <!-- List of files with certificates to use. Optional -->
            <certificates>
               <certificate>certs/cert-to-add.pem</certificate>
               <certificate>certs/cert-to-add.der</certificate>
            </certificates>
            
            <!-- List of URLs to download the certificates from. Optional -->
            <urls>
               <url>https://www.example.com</url>
            </urls>
            
         </configuration>
      </execution>
   </executions>
</plugin>
```

## Required Java version

Truststore Maven Plugin is tested against a broad range of Java versions.
See [Travis CI config](https://github.com/automatictester/truststore-maven-plugin/blob/master/.travis.yml) for details.
