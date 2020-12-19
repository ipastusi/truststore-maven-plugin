package uk.co.automatictester.truststore.maven.plugin.it;

import org.testng.annotations.Test;

import javax.net.ssl.SSLHandshakeException;

import static io.restassured.RestAssured.given;

public class ApiIT {

    private static final String TRUSTSTORE = "target/truststore_www_google_com.jks";
    private static final String TRUSTSTORE_PASSWORD = "changeit";

    @Test
    public void testHttpsRequest() {
        given().baseUri("https://www.google.com")
                .and().trustStore(TRUSTSTORE, TRUSTSTORE_PASSWORD)
                .when().get()
                .then().assertThat().statusCode(200);
    }

    @Test(expectedExceptions = SSLHandshakeException.class)
    public void testHttpsRequestWithoutServerCertsInTruststore() {
        given().baseUri("https://www.amazon.com")
                .and().trustStore(TRUSTSTORE, TRUSTSTORE_PASSWORD)
                .when().get();
    }
}
