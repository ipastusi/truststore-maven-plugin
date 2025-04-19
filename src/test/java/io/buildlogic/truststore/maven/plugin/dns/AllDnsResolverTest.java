package io.buildlogic.truststore.maven.plugin.dns;

import org.testng.annotations.Test;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class AllDnsResolverTest {

    @Test
    public void testResolveMultiple() {
        DnsResolver resolver = new AllDnsResolver(null);
        List<InetAddress> addresses = resolver.resolve("dns.google.com");
        assertThat(addresses.size()).isGreaterThan(1);
    }

    @Test
    public void testResolveSingle() {
        DnsResolver resolver = new AllDnsResolver(null);
        List<InetAddress> addresses = resolver.resolve("cisco.com");
        assertThat(addresses.size()).isEqualTo(1);
    }

    @Test
    public void testResolveCustom() throws UnknownHostException {
        Map<String, String> mapping = new HashMap<>() {{
            put("dns.google.com", "8.8.8.8");
        }};
        byte[] ip = new byte[]{8, 8, 8, 8};
        DnsResolver resolver = new AllDnsResolver(mapping);
        List<InetAddress> addresses = resolver.resolve("dns.google.com");
        assertThat(addresses.size()).isEqualTo(1);
        assertThat(addresses.get(0)).isEqualTo(InetAddress.getByAddress("dns.google.com", ip));
    }

    @Test
    public void testResolveCustomUndefined() {
        Map<String, String> mapping = new HashMap<>() {{
            put("google.com", "142.250.148.104");
        }};
        byte[] ip = new byte[]{(byte) 142, (byte) 250, (byte) 148, (byte) 104};
        DnsResolver resolver = new AllDnsResolver(mapping);
        List<InetAddress> addresses = resolver.resolve("cisco.com");
        assertThat(addresses.size()).isEqualTo(1);
        assertThat(addresses.get(0).getAddress()).isNotEqualTo(ip);
    }
}
