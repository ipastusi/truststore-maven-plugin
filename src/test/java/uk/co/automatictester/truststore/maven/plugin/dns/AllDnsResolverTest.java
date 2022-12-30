package uk.co.automatictester.truststore.maven.plugin.dns;

import org.testng.annotations.Test;

import java.net.InetAddress;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class AllDnsResolverTest {

    @Test
    public void testResolveMultiple() {
        DnsResolver resolver = new AllDnsResolver();
        List<InetAddress> addresses = resolver.resolve("amazon.com");
        assertThat(addresses.size()).isGreaterThan(1);
    }

    @Test
    public void testResolveSingle() {
        DnsResolver resolver = new AllDnsResolver();
        List<InetAddress> addresses = resolver.resolve("akamai.com");
        assertThat(addresses.size()).isEqualTo(1);
    }
}
