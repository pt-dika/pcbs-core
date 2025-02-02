package dck.pcbs.settlementfiledownload.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
public class RabbitMQService {

    RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.rfs-download.exchange}")
    private String rfsDownloadExchange;

    @Value("${rabbitmq.rfs-download.routingKey}")
    private String rfsDownloadRouting;

    @Autowired
    private RabbitMQService(RabbitTemplate amqpTemplate){
        this.rabbitTemplate = amqpTemplate;
    }

    public void publishRfsDownloaded(Map downloaded) {
        rabbitTemplate.convertAndSend(rfsDownloadExchange, rfsDownloadRouting, downloaded);
        log.info(rfsDownloadExchange);
        log.info(rfsDownloadRouting);
        log.info("Send msg = " + downloaded);
    }
}
