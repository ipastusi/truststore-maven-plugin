package uk.co.automatictester.truststore.maven.plugin.dns;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.Properties;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DnsResolverFactory {

    public static DnsResolver getInstance(DnsResolution dnsResolution, Map<String, String> mapping) {
        switch (dnsResolution) {
            case ALL:
                return new AllDnsResolver(mapping);
            case SINGLE:
                return new SingleDnsResolver(mapping);
            default:
                throw new RuntimeException("Unexpected DNS resolution option: " + dnsResolution);
        }
    }
}
