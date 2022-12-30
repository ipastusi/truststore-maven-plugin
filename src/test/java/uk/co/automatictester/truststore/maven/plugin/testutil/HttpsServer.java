package uk.co.automatictester.truststore.maven.plugin.testutil;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

public class HttpsServer {

    private final WireMockServer server;

    public HttpsServer() {
        this(false);
    }

    public HttpsServer(boolean withClientAuth) {
        WireMockConfiguration config = options()
                .keystorePath("src/test/resources/keystores/wiremock_server_key_cert.p12")
                .keystorePassword("password")
                .dynamicHttpsPort()
                .httpDisabled(true);

        if (withClientAuth) {
            config = addClientAuth(config);
        }

        server = new WireMockServer(config);
        server.start();
    }

    private WireMockConfiguration addClientAuth(WireMockConfiguration config) {
        return config
                .needClientAuth(true)
                .trustStorePath("src/test/resources/truststore/client_auth_cert.p12")
                .trustStorePassword("changeit");
    }

    public int port() {
        return server.httpsPort();
    }
}
