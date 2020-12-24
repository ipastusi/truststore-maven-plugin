package uk.co.automatictester.truststore.maven.plugin.config;

import org.testng.annotations.Test;
import uk.co.automatictester.truststore.maven.plugin.mojo.Truststore;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class TruststoresPropertyParserTest {

    @Test
    public void testParseSingle() {
        String property = "[file=truststores/truststore-1.p12,password=changeit]";
        List<Truststore> truststores = new TruststoresPropertyParser().parse(property);
        assertThat(truststores.size()).isEqualTo(1);

        Truststore t1 = new Truststore("truststores/truststore-1.p12", "changeit");
        assertThat(truststores).contains(t1);
    }

    @Test
    public void testParseMultiple() {
        String property = "[file=truststores/truststore-1.p12,password=changeit],[file=truststores/truststore-2.jks,password=topsecret]";
        List<Truststore> truststores = new TruststoresPropertyParser().parse(property);
        assertThat(truststores.size()).isEqualTo(2);

        Truststore t1 = new Truststore("truststores/truststore-1.p12", "changeit");
        assertThat(truststores).contains(t1);

        Truststore t2 = new Truststore("truststores/truststore-2.jks", "topsecret");
        assertThat(truststores).contains(t2);
    }

    @Test(expectedExceptions = RuntimeException.class, expectedExceptionsMessageRegExp = "Malformed property 'truststore.truststores': .*")
    public void testParseMalformed() {
        String property = "[file=truststores/truststore-1.p12,pass=changeit]";
        new TruststoresPropertyParser().parse(property);
    }
}
