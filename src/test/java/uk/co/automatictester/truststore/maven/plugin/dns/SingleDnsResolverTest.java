package uk.co.automatictester.truststore.maven.plugin.dns;

import org.testng.annotations.Test;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;

public class SingleDnsResolverTest {

    @Test
    public void testResolve() {
        DnsResolver resolver = new SingleDnsResolver(null);
        List<InetAddress> addresses = resolver.resolve("google.com");
        assertThat(addresses.size()).isEqualTo(1);
    }

    @Test
    public void testResolveCustom() throws UnknownHostException {
        Map<String, String> mapping = new HashMap<String, String>() {{
            put("google.com", "142.250.148.104");
        }};
        byte[] ip = new byte[]{(byte) 142, (byte) 250, (byte) 148, (byte) 104};
        DnsResolver resolver = new SingleDnsResolver(mapping);
        List<InetAddress> addresses = resolver.resolve("google.com");
        assertThat(addresses.size()).isEqualTo(1);
        assertThat(addresses.get(0)).isEqualTo(InetAddress.getByAddress("google.com", ip));
    }

    @Test
    public void testResolveCustomUndefined() {
        Map<String, String> mapping = new HashMap<String, String>() {{
            put("microsoft.com", "142.250.148.104");
        }};
        byte[] ip = new byte[]{(byte) 142, (byte) 250, (byte) 148, (byte) 104};
        DnsResolver resolver = new SingleDnsResolver(mapping);
        List<InetAddress> addresses = resolver.resolve("google.com");
        assertThat(addresses.size()).isEqualTo(1);
        assertThat(addresses.get(0).getAddress()).isNotEqualTo(ip);
    }
}
