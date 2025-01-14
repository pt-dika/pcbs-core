package dk.apps.pcps.config.mqtt;

import lombok.Data;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.net.SocketFactory;

@Data
@Configuration
@ConfigurationProperties("mqtt")
public class MqttProperties {
    private String certPath;
    private boolean automaticReconnect;
    private boolean cleanSession;
    private boolean ssl;
    private int connectionTimeout;
    private String clientId;
    private String hostname;
    private int port;
    private SocketFactory socketFactory;

    public MqttConnectOptions getMqttConnectOptions() {
        MqttConnectOptions options = new MqttConnectOptions();
        options.setCleanSession(this.isCleanSession());
        options.setConnectionTimeout(this.getConnectionTimeout());
        options.setSocketFactory(this.getSocketFactory());
        return options;
    }
}
