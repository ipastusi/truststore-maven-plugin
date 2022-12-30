package uk.co.automatictester.truststore.maven.plugin.testutil;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static uk.co.automatictester.truststore.maven.plugin.testutil.ConnectionHandlingRules.DISCONNECT;

@Slf4j
public class ConnectionClosingProxyServer {

    private static final String HOST = "127.0.0.1";
    private final Queue<ConnectionHandlingRules> connectionHandlingRules;
    private final int targetPort;
    private final CountDownLatch latch;

    @Getter
    private int port;

    public ConnectionClosingProxyServer(int targetPort, ConnectionHandlingRules[] connectionHandlingRules) {
        this.targetPort = targetPort;
        this.connectionHandlingRules = new LinkedList<>(Arrays.asList(connectionHandlingRules));
        this.latch = new CountDownLatch(1);
    }

    public int start() throws InterruptedException {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(this::run);
        latch.await();
        return port;
    }

    public void run() {
        log.info("Starting proxy {}:{} -> {}:{}", HOST, port, HOST, targetPort);
        ExecutorService executor = Executors.newSingleThreadExecutor();
        ServerSocket serverSocket = getServerSocket();
        port = serverSocket.getLocalPort();

        while (true) {
            Socket inSocket = null;
            Socket outSocket = null;
            try {
                latch.countDown();
                // blocks until a connection is made
                inSocket = serverSocket.accept();

                ConnectionHandlingRules closeConnection = connectionHandlingRules.poll();
                if (closeConnection != null && closeConnection.equals(DISCONNECT)) {
                    log.info("Connection closed");
                    inSocket.close();
                    continue;
                }

                InputStream clientIn = inSocket.getInputStream();
                OutputStream clientOut = inSocket.getOutputStream();

                outSocket = new Socket(HOST, targetPort);
                InputStream serverIn = outSocket.getInputStream();
                OutputStream serverOut = outSocket.getOutputStream();

                Runnable processClientRequests = () -> {
                    write(clientIn, serverOut);
                };
                executor.execute(processClientRequests);

                write(serverIn, clientOut);
                log.info("Connection processed");
            } catch (IOException e) {
                log.error("Error processing connection: {}", e.getMessage());
            } finally {
                try {
                    if (inSocket != null) {
                        inSocket.close();
                    }
                    if (outSocket != null) {
                        outSocket.close();
                    }
                } catch (IOException e) {
                    log.error("Error closing sockets: {}", e.getMessage());
                }
            }
        }
    }

    private ServerSocket getServerSocket() {
        try {
            return new ServerSocket(0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void write(InputStream in, OutputStream out) {
        byte[] buffer = new byte[1024];
        int len;
        try {
            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
        } catch (IOException e) {
            log.error("Error converting streams: {}", e.getMessage());
        }
    }
}
