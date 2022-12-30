package uk.co.automatictester.truststore.maven.plugin.dns;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class AllDnsResolver implements DnsResolver {

    @Override
    public List<InetAddress> resolve(String host) {
        try {
            InetAddress[] addresses = InetAddress.getAllByName(host);
            List<InetAddress> ipv4Addresses = new ArrayList<>();
            for (InetAddress address : addresses) {
                if (address instanceof Inet4Address) {
                    ipv4Addresses.add(address);
                }
            }
            if (ipv4Addresses.size() == 0) {
                throw new RuntimeException("No IPv4 address found for host: " + host);
            }
            return ipv4Addresses;
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }
}
