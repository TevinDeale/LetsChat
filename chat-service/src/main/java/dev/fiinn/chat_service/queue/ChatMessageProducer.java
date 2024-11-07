package dev.fiinn.chat_service.queue;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class
ChatMessageProducer {
    private final RabbitTemplate rabbitTemplate;
    private final RetryTemplate retryTemplate;

    private final String EXCHANGE_NAME = "chat_exchange";
    private final String ROUTING_KEY = "chat.message.";

    public ChatMessageProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
        this.retryTemplate = createRetryTemplate();
    }

    private RetryTemplate createRetryTemplate() {
        RetryTemplate retryTemplate = new RetryTemplate();

        SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy();
        retryPolicy.setMaxAttempts(3);

        FixedBackOffPolicy backOffPolicy = new FixedBackOffPolicy();
        backOffPolicy.setBackOffPeriod(1000);

        retryTemplate.setRetryPolicy(retryPolicy);
        retryTemplate.setBackOffPolicy(backOffPolicy);

        return retryTemplate;
    }

    public void sendMessage(ChatMessageEvent event) {

        try {
            retryTemplate.execute(context -> {
                rabbitTemplate.convertAndSend(EXCHANGE_NAME,ROUTING_KEY + event.getThreadId());
                return null;
            });
        } catch (Exception err) {
            log.error("Failed to send message after retries", err);
        }
    }
}
