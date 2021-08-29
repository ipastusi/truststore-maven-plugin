package uk.co.automatictester.truststore.maven.plugin.file;

import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class FileCheckerTest {

    @Test
    public void testIsReadableFileTrue() {
        boolean isReadable = FileChecker.isReadableFile("pom.xml");
        assertThat(isReadable).isTrue();
    }

    @Test
    public void testIsReadableFileDir() {
        boolean isReadable = FileChecker.isReadableFile("src");
        assertThat(isReadable).isFalse();
    }

    @Test
    public void testIsReadableFileNonexistent() {
        boolean isReadable = FileChecker.isReadableFile("poom.xml");
        assertThat(isReadable).isFalse();
    }
}
