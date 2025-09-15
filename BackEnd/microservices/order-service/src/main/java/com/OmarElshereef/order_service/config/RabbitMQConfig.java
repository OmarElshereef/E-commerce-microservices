package com.OmarElshereef.order_service.config;

import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;


@Configuration
@EnableRabbit
public class RabbitMQConfig {

    // Exchanges
    @Bean
    public TopicExchange orderExchange() {
        return new TopicExchange("order.events");
    }

    @Bean
    public TopicExchange paymentExchange() {
        return new TopicExchange("payment.events");
    }

    // Queues that Order Service listens to
    @Bean
    public Queue paymentSuccessQueue() {
        return QueueBuilder.durable("payment.success").build();
    }

    // Binding for payment events
    @Bean
    public Binding paymentSuccessBinding() {
        return BindingBuilder
                .bind(paymentSuccessQueue())
                .to(paymentExchange())
                .with("payment.success");
    }

    // JSON converter
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(new Jackson2JsonMessageConverter());
        return template;
    }
}
