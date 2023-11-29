package io.buildlogic.truststore.maven.plugin.mojo;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CustomScryptConfigTest {

    @DataProvider(name = "incompleteScryptConfigs")
    public Object[][] incompleteScryptConfigs() {
        return new CustomScryptConfig[][]{
                {new CustomScryptConfig(null, 2, 3, 4)},
                {new CustomScryptConfig(1, null, 3, 4)},
                {new CustomScryptConfig(1, 2, null, 4)},
                {new CustomScryptConfig(1, 2, 3, null)},
        };
    }

    @Test(dataProvider = "incompleteScryptConfigs", expectedExceptions = RuntimeException.class,
            expectedExceptionsMessageRegExp = "Incomplete Scrypt configuration.*")
    public void testValidate(CustomScryptConfig config) {
        config.validate();
    }

    @Test
    public void testToString() {
        CustomScryptConfig config = new CustomScryptConfig(1, 2, 3, 4);
        assertThat(config.toString()).isEqualTo("costParameter: 1, blockSize: 2, parallelizationParameter: 3, saltLength: 4");
    }
}
