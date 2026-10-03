package com.hrgenius.attendance_service.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${app.rabbitmq.exchange}")
    private String exchange;

    @Value("${app.rabbitmq.queue}")
    private String queue;

    @Value("${app.rabbitmq.routing-key-in}")
    private String routingKeyIn;

    @Value("${app.rabbitmq.routing-key-out}")
    private String routingKeyOut;

    @Bean
    public Queue attendanceQueue() {
        // durable = true (server restart hone par message delete nahi hoga)
        return new Queue(queue, true);
    }

    @Bean
    public TopicExchange attendanceExchange() {
        return new TopicExchange(exchange);
    }

    @Bean
    public Binding bindingPunchIn(Queue attendanceQueue, TopicExchange attendanceExchange) {
        return BindingBuilder.bind(attendanceQueue).to(attendanceExchange).with("attendance.punch.*");
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }
}