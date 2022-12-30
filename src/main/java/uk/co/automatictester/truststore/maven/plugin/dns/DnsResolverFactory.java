package uk.co.automatictester.truststore.maven.plugin.dns;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DnsResolverFactory {

    public static DnsResolver getInstance(DnsResolution dnsResolution) {
        switch (dnsResolution) {
            case ALL:
                return new AllDnsResolver();
            case SINGLE:
                return new SingleDnsResolver();
            default:
                throw new RuntimeException("Unexpected DNS resolution option: " + dnsResolution);
        }
    }
}
