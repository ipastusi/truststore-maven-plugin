package io.buildlogic.truststore.maven.plugin.dns;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;

public class AllDnsResolver implements DnsResolver {

    private final CustomDnsResolver customResolver;

    public AllDnsResolver(Map<String, String> mappings) {
        customResolver = new CustomDnsResolver(mappings);
    }

    @Override
    public List<InetAddress> resolve(String host) {
        try {
            Optional<InetAddress> customAddress = customResolver.resolve(host);
            if (customAddress.isPresent()) {
                return Collections.singletonList(customAddress.get());
            }
            InetAddress[] allAddresses = InetAddress.getAllByName(host);
            List<InetAddress> ipv4Addresses = new ArrayList<>();
            for (InetAddress address : allAddresses) {
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
