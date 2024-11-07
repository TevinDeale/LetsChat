package dev.fiinn.chat_service.queue;

import dev.fiinn.chat_service.exception.MessageNotFoundException;
import dev.fiinn.chat_service.model.Message;
import dev.fiinn.chat_service.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatMessageConsumer {
    private static final Logger log = LoggerFactory.getLogger(ChatMessageConsumer.class);
    private final MessageService messageService;

    @RabbitListener(queues = "chat_messages")
    @Retryable(
            maxAttempts = 3,
            backoff = @Backoff(delay = 1000, multiplier = 2),
            exclude = {MessageNotFoundException.class}
    )
    public void receiveMessage(ChatMessageEvent event) {
        try {
            Message message = messageService.findById(event.getMessageId());

            messageService.markMessageAsSent(message.getMessageId(), message.getUserId());

        } catch (Exception err) {
            log.error("Error processing message: {}", event.getMessageId(), err);
            throw err;
        }
    }
}
