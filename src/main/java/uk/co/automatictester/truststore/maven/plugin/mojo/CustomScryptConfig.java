package uk.co.automatictester.truststore.maven.plugin.mojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomScryptConfig {
    private Integer costParameter;
    private Integer blockSize;
    private Integer parallelizationParameter;
    private Integer saltLength;

    public boolean isInitialized() {
        return costParameter != null
                && blockSize != null
                && parallelizationParameter != null
                && saltLength != null;
    }
}
