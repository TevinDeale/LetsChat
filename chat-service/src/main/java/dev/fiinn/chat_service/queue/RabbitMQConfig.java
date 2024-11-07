package dev.fiinn.chat_service.queue;

import com.rabbitmq.client.AMQP;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    Queue chatQueue() {
        return QueueBuilder.durable("chat_messages")
                .withArgument("x-dead-letter-exchange", "chat.dlx")
                .withArgument("x-dead-letter-routing-key", "chat.message.dead")
                .build();
    }

    @Bean
    TopicExchange chatExchange() {
        return new TopicExchange("chat_exchange");
    }

    @Bean
    Binding binding(Queue chatQueue, TopicExchange chatExchange) {
        return BindingBuilder
                .bind(chatQueue)
                .to(chatExchange)
                .with("chat.message.*");
    }

    @Bean
    Queue deadLetterQueue() {
        return new Queue("chat_message.dlq");
    }

    @Bean
    DirectExchange deadLetterExchange() {
        return new DirectExchange("chat.dlx");
    }

    @Bean
    Binding deadLetterBinding() {
        return BindingBuilder.bind(deadLetterQueue())
                .to(deadLetterExchange())
                .with("chat.message.dead");
    }
}
