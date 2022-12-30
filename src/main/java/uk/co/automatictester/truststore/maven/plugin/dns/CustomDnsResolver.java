package uk.co.automatictester.truststore.maven.plugin.dns;

import lombok.RequiredArgsConstructor;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
public class CustomDnsResolver {

    private final Map<String, String> mapping;

    public Optional<InetAddress> resolve(String host) throws UnknownHostException {
        if (mapping != null && mapping.containsKey(host)) {
            String address = mapping.get(host);
            byte[] addressAsBytes = toByteArray(address);
            InetAddress inetAddress = InetAddress.getByAddress(host, addressAsBytes);
            return Optional.of(inetAddress);
        }
        return Optional.empty();
    }

    byte[] toByteArray(String address) throws UnknownHostException {
        InetAddress ip = InetAddress.getByName(address);
        return ip.getAddress();
    }
}
