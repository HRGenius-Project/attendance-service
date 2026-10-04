package com.hrgenius.attendance_service.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
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
    private String attendanceQueue;

    @Value("${app.rabbitmq.routing-key-in}")
    private String routingKeyIn;

    @Value("${app.rabbitmq.routing-key-out}")
    private String routingKeyOut;

    @Value("${app.rabbitmq.leave-queue}")
    private String leaveQueue;

    @Value("${app.rabbitmq.routing-key-leave}")
    private String routingKeyLeave;

    @Bean
    public TopicExchange attendanceExchange() {
        return new TopicExchange(exchange);
    }

    @Bean
    public Queue attendanceQueue() {
        return new Queue(attendanceQueue, true);
    }

    @Bean
    public Queue attendanceLeaveQueue() {
        return new Queue(leaveQueue, true);
    }

    @Bean
    public Binding bindingPunchIn(Queue attendanceQueue, TopicExchange attendanceExchange) {
        return BindingBuilder.bind(attendanceQueue).to(attendanceExchange).with(routingKeyIn);
    }

    @Bean
    public Binding bindingPunchOut(Queue attendanceQueue, TopicExchange attendanceExchange) {
        return BindingBuilder.bind(attendanceQueue).to(attendanceExchange).with(routingKeyOut);
    }

    @Bean
    public Binding bindingLeaveApproved(Queue attendanceLeaveQueue, TopicExchange attendanceExchange) {
        return BindingBuilder.bind(attendanceLeaveQueue).to(attendanceExchange).with(routingKeyLeave);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}