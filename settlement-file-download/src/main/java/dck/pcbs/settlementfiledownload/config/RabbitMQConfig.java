package dck.pcbs.settlementfiledownload.config;

import dck.pcbs.commons.handler.FatalExceptionStrategy;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.listener.ConditionalRejectingErrorHandler;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ErrorHandler;

//@Configuration
public class RabbitMQConfig {

    @Value("${rabbitmq.rfs-download.queue}")
    private String messageQueue;
    @Value("${rabbitmq.rfs-download.exchange}")
    private String messageExchange;
    @Value("${rabbitmq.rfs-download.routingKey}")
    private String messageRoutingKey;

    @Bean
    Queue queue() {
        return new Queue(messageQueue, true, false, false);
    }

//    @Bean
//    public ErrorHandler errorHandler(){
//        return new ConditionalRejectingErrorHandler(new FatalExceptionStrategy());
//    }

    @Bean
    DirectExchange exchange() {
        return new DirectExchange(messageExchange);
    }

    @Bean
    Binding binding(Queue queue1, DirectExchange exchange1) {
        return BindingBuilder.bind(queue1).to(exchange1).with(messageRoutingKey);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
