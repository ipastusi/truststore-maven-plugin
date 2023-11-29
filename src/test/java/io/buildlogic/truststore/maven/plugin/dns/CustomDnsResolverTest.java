package io.buildlogic.truststore.maven.plugin.dns;

import org.testng.annotations.Test;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class CustomDnsResolverTest {

    @Test
    public void testResolve() throws UnknownHostException {
        Map<String, String> mapping = new HashMap<String, String>() {{
            put("oracle.com", "142.251.2.101");
            put("microsoft.com", "142.251.2.102");
        }};
        CustomDnsResolver resolver = new CustomDnsResolver(mapping);
        byte[] ip = new byte[]{(byte) 142, (byte) 251, (byte) 2, (byte) 101};
        InetAddress oracleAddress = InetAddress.getByAddress("oracle.com", ip);
        assertThat(resolver.resolve("oracle.com")).isEqualTo(Optional.of(oracleAddress));
    }

    @Test
    public void testResolveUndefined() throws UnknownHostException {
        Map<String, String> mapping = new HashMap<String, String>() {{
            put("google.com", "142.251.2.101");
            put("microsoft.com", "142.251.2.102");
        }};
        CustomDnsResolver resolver = new CustomDnsResolver(mapping);
        assertThat(resolver.resolve("akamai.com")).isEmpty();
    }

    @Test
    public void testResolveEmpty() throws UnknownHostException {
        Map<String, String> mapping = new HashMap<>();
        CustomDnsResolver resolver = new CustomDnsResolver(mapping);
        assertThat(resolver.resolve("akamai.com")).isEmpty();
    }

    @Test
    public void testResolveNull() throws UnknownHostException {
        Map<String, String> mapping = null;
        CustomDnsResolver resolver = new CustomDnsResolver(mapping);
        assertThat(resolver.resolve("akamai.com")).isEmpty();
    }
}
