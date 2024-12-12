package com.example.email.rabbitmq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>Configuration class for RabbitMQ messaging setup.</p>
 * <p>This class defines the beans for queue, exchange, and binding configurations required for messaging.</p>
 */
@Configuration
public class RabbitMQConfig {

    /**
     * The name of the RabbitMQ queue, injected from the application properties.
     */
    @Value("${rabbitmq.queue.name}")
    private String queue;

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
     * Defines the RabbitMQ queue.
     *
     * @return a {@link Queue} object representing the configured queue.
     */
    @Bean
    public Queue queue() {
        return new Queue(queue);
    }

    /**
     * Defines the RabbitMQ topic exchange.
     *
     * @return a {@link TopicExchange} object representing the configured exchange.
     */
    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(exchange);
    }

    /**
     * Defines the binding between the queue and the exchange using the routing key.
     *
     * @return a {@link Binding} object representing the binding configuration.
     */
    @Bean
    public Binding binding() {
        return BindingBuilder.bind(queue())
                .to(exchange())
                .with(routingKey);
    }
}
