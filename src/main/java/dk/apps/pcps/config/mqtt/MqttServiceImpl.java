package dk.apps.pcps.config.mqtt;


import dk.apps.pcps.PcpsAppsApplication;
import dk.apps.pcps.commonutils.SocketFactory;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

@Slf4j
@Service
public class MqttServiceImpl implements IMqttService {

    private IMqttClient mqttClient;

    MqttProperties prop;
//    ListenNotificationService listenNotificationService;

    @Autowired
    public MqttServiceImpl(MqttProperties prop){
        this.prop = prop;
        //this.listenNotificationService = listenNotificationService;
    }

    @Override
    public boolean connect() throws MqttException {
        boolean isSsl = prop.isSsl();
        String connection = isSsl ? "ssl://" : "tcp://";
        mqttClient = new MqttClient(connection + prop.getHostname() + ":" + prop.getPort(), prop.getClientId(), new MemoryPersistence());
        if (isSsl) {
            SocketFactory.SocketFactoryOptions socketFactoryOptions = new SocketFactory.SocketFactoryOptions();
            try {
                InputStream stream = new PcpsAppsApplication().getClass().getResourceAsStream(prop.getCertPath());
                socketFactoryOptions.withCaInputStream(stream);
                prop.setSocketFactory(new SocketFactory(socketFactoryOptions));
//                listenNotificationService.setService(this);
//                mqttClient.setCallback(listenNotificationService);
            } catch (IOException | NoSuchAlgorithmException | KeyStoreException | CertificateException | KeyManagementException | UnrecoverableKeyException e) {
                log.info("Exception  " + mqttClient.getServerURI());
                log.error(e.getMessage());
                e.printStackTrace();
            }
        }
        mqttClient.connect(prop.getMqttConnectOptions());
        return mqttClient.isConnected();
    }

    @Override
    public boolean isConnected() {
        return mqttClient.isConnected();
    }

    @Override
    public String getClient() {
        return mqttClient.getClientId();
    }

    @Override
    public void publish(String topic, String message) throws MqttException {
        MqttMessage mqttMessage = new MqttMessage();
        mqttMessage.setPayload(message.getBytes());
        mqttClient.publish(topic, mqttMessage);
    }

    @Override
    public void subscribe(String topic) throws MqttException {
        mqttClient.subscribe(topic);
    }

    @Override
    public void unsubscribe(String topic) throws MqttException {
        mqttClient.unsubscribe(topic);
    }

    @Override
    public void disconnect() throws MqttException {
        mqttClient.disconnect();
    }

    @Bean
    public void autoConnect() throws MqttException {
        this.connect();
    }
}
