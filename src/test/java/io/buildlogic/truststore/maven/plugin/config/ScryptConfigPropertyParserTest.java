package io.buildlogic.truststore.maven.plugin.config;

import org.testng.annotations.Test;
import io.buildlogic.truststore.maven.plugin.mojo.CustomScryptConfig;

import static org.assertj.core.api.Assertions.assertThat;

public class ScryptConfigPropertyParserTest {

    @Test
    public void testParse() {
        String property = "costParameter=1,blockSize=22,parallelizationParameter=333,saltLength=4444";
        CustomScryptConfig actualScryptConfig = new ScryptConfigPropertyParser().parse(property);

        CustomScryptConfig expectedScryptConfig = new CustomScryptConfig(1, 22, 333, 4444);
        assertThat(actualScryptConfig).isEqualTo(expectedScryptConfig);
    }

    @Test(expectedExceptions = RuntimeException.class, expectedExceptionsMessageRegExp = "Malformed property 'truststore.scryptConfig': .*")
    public void testParseMalformed() {
        String property = "[costParameter=1,blockSize=22,parallelizationParameter=333,salt=5]";
        new ScryptConfigPropertyParser().parse(property);
    }
}
