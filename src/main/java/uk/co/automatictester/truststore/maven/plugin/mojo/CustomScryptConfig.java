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

    public void validate() {
        if (!isComplete()) {
            throw new RuntimeException("Incomplete Scrypt configuration: " + this);
        }
    }

    private boolean isComplete() {
        return costParameter != null
                && blockSize != null
                && parallelizationParameter != null
                && saltLength != null;
    }

    @Override
    public String toString() {
        return String.format("costParameter: %d, blockSize: %d, parallelizationParameter: %d, saltLength: %d",
                costParameter, blockSize, parallelizationParameter, saltLength);
    }
}
