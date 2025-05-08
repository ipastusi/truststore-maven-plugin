# 6.1.0

Date: 8 May 2025

- Compiled against BouncyCastle `bcpkix-jdk18on` version `1.80`.
- Upgraded dependency versions.

# 6.0.0

Date: 14 Dec 2023

- **BREAKING CHANGE**: Java 11 is minimum supported version now.
- `groupId` has changed to `io.buildlogic`.
- Compiled against BouncyCastle `bcpkix-jdk18on` version `1.77`.
- Upgraded dependency versions.

# 5.0.0

Date: 27 Mar 2023

- **BREAKING CHANGE**: BouncyCastle dependency is no longer bundled with the plugin. If you want to use truststore formats supported by BouncyCastle, you can
  define the dependency explicitly. See readme for more details.

# 4.0.0

Date: 31 Dec 2022

- **BREAKING CHANGE**: Config option `urls` is now removed. Switch to using `servers` instead.
- **BREAKING CHANGE**: Config option `skipHostnameVerification` is now removed, as it was used only together with `urls`.
- Added support for downloading certificates from all IP addresses the hostname resolves to. This is now the default behaviour.
- Added support for retrying when downloading certificates. This is now enabled by default.
- Added support for configuring timeouts when downloading certificates. Default timeout is now 10000ms (10s).
- Added support for configuring custom DNS resolution when downloading certificates.

# 3.0.0

Date: 10 Apr 2022

- **BREAKING CHANGE**: Changed default output truststore format from JKS to PKCS12.
- Added support for downloading X.509 certificates from any TLS servers, not only HTTPS.
- Added support for reading truststores in BKS, JCEKS and UBER formats.
- Added support for generating truststores in BKS, JCEKS and UBER formats.
- Added support for using custom Scrypt config when generating truststores in BCFKS format.
- **DEPRECATED**: Config option `urls` is now deprecated and will be removed in future versions. Switch to using `servers` instead.

# 2.6.0

Date: 20 Mar 2022

- Added support for truststores in BCFKS format.

# 2.5.0

Date: 24 Dec 2021

- Improved logging.

# 2.4.0

Date: 29 Aug 2021

- Added `includeDefaultTruststore` option which allows to include in target truststore certificates from the default Java truststore in either
  `<java.home>/lib/security/jssecacerts` or `<java.home>/lib/security/cacerts` (in this order).
- Added `skip` option which allows to skip plugin execution.
- Fixed a defect which prevented more than one certificate with the same serial number from being included in the target truststore.
  [RFC 5280](https://datatracker.ietf.org/doc/html/rfc5280#section-4.1.2.2) requires the serial numbers to be unique only across certificates issued by a given
  CA.

# 2.3.0

Date: 28 Jun 2021

- X.509 certificates with Subject Alternative Name (SAN) extension will have their SANs listed in the build log.

# 2.2.2

Date: 24 Jan 2021

- Fixed duplicated certificates being listed in console output, if e.g. same input url was provided more than once. It was only the output that could contain
  same certificate more than once, not the generated truststore.

# 2.2.1

Date: 7 Jan 2021

- Fixed an issue with downloading certificates when `trustAllCertificates` was set to `true`, server required client authentication and keystore with correct
  client certificate was used. This was caused by always using empty keystore when `trustAllCertificates` was set to `true`, and therefore never producing a
  client certificate in response to server's CertificateRequest TLS message. Now keystore is used if specified.

# 2.2.0

Date: 31 Dec 2020

- Added support for filtering downloaded X.509 certificates using `includeCertificates`.
- Fixed config option name `trustAllCertificates` to be in line with the specification.

# 2.1.0

Date: 26 Dec 2020

- Added support for loading X.509 certificates from pre-existing truststores.
- Improved config validation and error handling.

# 2.0.0

Date: 23 Dec 2020

- **BREAKING CHANGE**: Added `truststore` prefix to all properties.
- Added support for reading multiple X.509 certificates from a single file.

# 1.0.0

Date: 20 Dec 2020

- Initial release.
