package uk.co.automatictester.truststore.maven.plugin.dns;

import java.net.InetAddress;
import java.util.List;

public interface DnsResolver {
    List<InetAddress> resolve(String host);
}
