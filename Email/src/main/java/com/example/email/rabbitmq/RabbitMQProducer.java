package com.example.email.rabbitmq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * <p>Service for producing and sending messages to RabbitMQ.</p>
 * <p>This service is responsible for sending messages to a specified exchange using a routing key.</p>
 */
@Service
public class RabbitMQProducer {

    /**
     * The name of the RabbitMQ exchange, injected from the application properties.
     */
    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    /**
     * The RabbitMQ routing key, injected from the application properties.
     */
    @Value("${rabbitmq.routing.key}")
    private String routingKey;

    /**
     * Logger instance for logging information related to message production.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMQProducer.class);

    /**
     * The {@link RabbitTemplate} used for interacting with RabbitMQ.
     */
    private RabbitTemplate rabbitTemplate;

    /**
     * Constructor for injecting the RabbitTemplate dependency.
     *
     * @param rabbitTemplate the {@link RabbitTemplate} to use for sending messages.
     */
    public RabbitMQProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    /**
     * Sends a message to the RabbitMQ exchange with the configured routing key.
     * <p>The message is logged before being sent to RabbitMQ.</p>
     *
     * @param message the message to be sent to RabbitMQ.
     */
    public void sendMessage(String message) {
        LOGGER.info(String.format("Message sent -> %s", message));
        rabbitTemplate.convertAndSend(exchange, routingKey, message);
    }
}
