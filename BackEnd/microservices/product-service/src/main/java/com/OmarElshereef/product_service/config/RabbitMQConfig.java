package com.OmarElshereef.product_service.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@EnableRabbit
public class RabbitMQConfig {

    // Order events exchange (we don't create it, but we reference it)
    @Bean
    public TopicExchange orderExchange() {
        return new TopicExchange("order.events");
    }

    // Queue that Product Service listens to
    @Bean
    public Queue stockReservationQueue() {
        return QueueBuilder.durable("product.reserve.stock").build();
    }

    // Binding: order.created events → stock reservation queue
    @Bean
    public Binding stockReservationBinding() {
        return BindingBuilder
                .bind(stockReservationQueue())
                .to(orderExchange())
                .with("order.created");
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(new Jackson2JsonMessageConverter());
        return template;
    }
}
