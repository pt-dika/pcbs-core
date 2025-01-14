package dk.apps.pcps.model.iso8583;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties("iso8583")
public class Iso8583Properties {
    private String host;
    private Integer port;
    private Integer timeout;
    private String packager;
    private Integer nii;
    private boolean showSensitiveData;
}
