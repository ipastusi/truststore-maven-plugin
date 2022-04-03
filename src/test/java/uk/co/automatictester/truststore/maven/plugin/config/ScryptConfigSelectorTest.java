package uk.co.automatictester.truststore.maven.plugin.config;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import uk.co.automatictester.truststore.maven.plugin.mojo.CustomScryptConfig;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ScryptConfigSelectorTest {

    private static final String MOCKED_SCRYPT_CONFIG_PROPERTY = "MOCKED_SCRYPT_CONFIG_PROPERTY";
    private CustomScryptConfig configScryptConfig;
    private CustomScryptConfig parsedScryptConfig;
    private ScryptConfigSelector scryptConfigSelector;

    @BeforeClass
    public void setUp() {
        configScryptConfig = new CustomScryptConfig(1, 1, 1, 1);
        parsedScryptConfig = new CustomScryptConfig(2, 2, 2, 2);

        ScryptConfigPropertyParser parser = mock(ScryptConfigPropertyParser.class);
        when(parser.parse(MOCKED_SCRYPT_CONFIG_PROPERTY)).thenReturn(parsedScryptConfig);

        scryptConfigSelector = new ScryptConfigSelector(parser);
    }

    @Test
    public void testConfig() {
        CustomScryptConfig selected = scryptConfigSelector.select(configScryptConfig, MOCKED_SCRYPT_CONFIG_PROPERTY);
        assertThat(selected).isEqualTo(configScryptConfig);
    }

    @Test
    public void testProperty() {
        CustomScryptConfig selected = scryptConfigSelector.select(null, MOCKED_SCRYPT_CONFIG_PROPERTY);
        assertThat(selected).isEqualTo(parsedScryptConfig);
    }

    @Test
    public void testNoneProvided() {
        CustomScryptConfig selected = scryptConfigSelector.select(null, null);
        assertThat(selected).isEqualTo(new CustomScryptConfig());
    }
}
