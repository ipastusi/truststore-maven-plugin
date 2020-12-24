package uk.co.automatictester.truststore.maven.plugin.config;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class URLValidatorTest {

    private final URLValidator URLValidator = new URLValidator();

    @DataProvider(name = "urls")
    public Object[][] urls() {
        return new String[][]{
                {"http://google.com"},
                {"google.com"},
        };
    }

    @Test(dataProvider = "urls", expectedExceptions = RuntimeException.class, expectedExceptionsMessageRegExp = "Invalid protocol in URL .*")
    public void testValidateProtocol(String url) {
        URLValidator.validate(url);
    }
}
