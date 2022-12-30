package uk.co.automatictester.truststore.maven.plugin.dns;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class SingleDnsResolver implements DnsResolver {

    private final CustomDnsResolver customResolver;

    public SingleDnsResolver(Map<String, String> mappings) {
        customResolver = new CustomDnsResolver(mappings);
    }

    @Override
    public List<InetAddress> resolve(String host) {
        try {
            Optional<InetAddress> customAddress = customResolver.resolve(host);
            if (customAddress.isPresent()) {
                return Collections.singletonList(customAddress.get());
            }
            return Collections.singletonList(InetAddress.getByName(host));
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }
}
