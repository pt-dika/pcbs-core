package dk.apps.pcps.config.sftp;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties("sftp")
public class SftpProperties {
    private String host;
    private int port;
    private String username;
    private String password;
    private int sessionTimeout;
    private int channelTimeout;
}
