Wiremock server is configured to:

- (a) serve content over HTTPS
- (b) require client authentication

This is achieved through:

- (a) `server/keystore/wiremock_server_key_cert.p12`
- (b) `server/truststore/client_auth_cert.p12`

For client authentication to work:

- client keystore: `client/keystore/client_auth_key_cert.p12`
- server truststore: `server/truststore/client_auth_cert.p12`
