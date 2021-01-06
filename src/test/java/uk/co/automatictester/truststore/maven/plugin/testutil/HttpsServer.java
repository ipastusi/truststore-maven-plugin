package uk.co.automatictester.truststore.maven.plugin.testutil;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;

import java.io.IOException;
import java.net.ServerSocket;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

public class HttpsServer {

    private final WireMockServer server;

    public HttpsServer() {
        this(false);
    }

    public HttpsServer(boolean withClientAuth) {
        ServerSocket serverSocket;
        int port;
        try {
            serverSocket = new ServerSocket(0);
            port = serverSocket.getLocalPort();
            serverSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        WireMockConfiguration config = options()
                .httpDisabled(true)
                .httpsPort(port);

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
                .trustStorePassword("changeit")
                .trustStoreType("PKCS12");
    }

    public int port() {
        return server.httpsPort();
    }

    public void stop() {
        if (server != null) {
            server.stop();
        }
    }
}
