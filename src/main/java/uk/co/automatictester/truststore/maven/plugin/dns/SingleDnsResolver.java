package uk.co.automatictester.truststore.maven.plugin.dns;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;

public class SingleDnsResolver implements DnsResolver {

    @Override
    public List<InetAddress> resolve(String host) {
        try {
            InetAddress[] addresses = {InetAddress.getByName(host)};
            return Arrays.asList(addresses);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }
}
