package uk.co.automatictester.truststore.maven.plugin.mojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Truststore {
    private String file;
    private String password;
}
