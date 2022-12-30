package uk.co.automatictester.truststore.maven.plugin.dns;

import org.testng.annotations.Test;

import java.net.InetAddress;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class SingleDnsResolverTest {

    @Test
    public void testResolve() {
        DnsResolver resolver = new SingleDnsResolver();
        List<InetAddress> addresses = resolver.resolve("google.com");
        assertThat(addresses.size()).isEqualTo(1);
    }
}
