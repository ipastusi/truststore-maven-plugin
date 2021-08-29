package uk.co.automatictester.truststore.maven.plugin.file;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FileChecker {

    public static boolean isReadableFile(String file) {
        Path path = Paths.get(file);
        return Files.isRegularFile(path) && Files.isReadable(path);
    }
}
