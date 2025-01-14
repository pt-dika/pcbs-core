package dk.apps.pcps.config.mqtt;

import org.eclipse.paho.client.mqttv3.MqttException;

public interface IMqttService {
    boolean connect() throws MqttException;
    boolean isConnected() throws MqttException;
    String getClient();
    void publish(String topic, String message) throws MqttException;
    void subscribe(String topic) throws MqttException;
    void unsubscribe(String topic) throws MqttException;
    void disconnect() throws MqttException;
}
