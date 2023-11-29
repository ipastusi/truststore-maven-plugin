package io.buildlogic.truststore.maven.plugin.config;

import org.mockito.Mockito;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import io.buildlogic.truststore.maven.plugin.mojo.Truststore;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TruststoreSelectorTest {

    private static final String MOCKED_TRUSTSTORES_PROPERTY = "MOCKED_TRUSTSTORES_PROPERTY";
    private List<Truststore> configTruststores;
    private List<Truststore> parsedTruststores;
    private TruststoreSelector truststoreSelector;

    @BeforeClass
    public void setUp() {
        Truststore t1 = new Truststore("f1", "p1");
        Truststore t2 = new Truststore("f2", "p2");
        Truststore t3 = new Truststore("f3", "p3");

        configTruststores = new ArrayList<>();
        configTruststores.add(t1);
        configTruststores.add(t2);

        parsedTruststores = new ArrayList<>();
        parsedTruststores.add(t3);

        TruststoresPropertyParser parser = Mockito.mock(TruststoresPropertyParser.class);
        when(parser.parse(MOCKED_TRUSTSTORES_PROPERTY)).thenReturn(parsedTruststores);

        truststoreSelector = new TruststoreSelector(parser);
    }

    @Test
    public void testConfig() {
        List<Truststore> selected = truststoreSelector.select(configTruststores, MOCKED_TRUSTSTORES_PROPERTY);
        assertThat(selected).isEqualTo(configTruststores);
    }

    @Test
    public void testProperty() {
        List<Truststore> selected = truststoreSelector.select(null, MOCKED_TRUSTSTORES_PROPERTY);
        assertThat(selected).isEqualTo(parsedTruststores);
    }

    @Test
    public void testNoneProvided() {
        List<Truststore> selected = truststoreSelector.select(null, null);
        assertThat(selected).isEqualTo(new ArrayList<>());
    }
}
