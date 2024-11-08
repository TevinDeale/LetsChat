package dev.fiinn.chat_service.websocket;

import dev.fiinn.chat_service.dto.MessageDto;
import dev.fiinn.chat_service.exception.MessageErrorException;
import dev.fiinn.chat_service.exception.RequiredPermissionsException;
import dev.fiinn.chat_service.model.ChatThread;
import dev.fiinn.chat_service.model.Message;
import dev.fiinn.chat_service.service.ChatThreadService;
import dev.fiinn.chat_service.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ChatWebSocketController {

    private final MessageService messageService;
    private final ChatThreadService chatThreadService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat/thread/{threadId}/message")
    public void handleChatMessage(@DestinationVariable String threadId,
                                  @Payload WebSocketMessage chatMessage) {
        log.debug("Received message for thread: {}", threadId);

        try {
            if (!chatThreadService.isUserInThread(threadId, chatMessage.getSender())) {
                throw new RequiredPermissionsException("User is not in this thread");
            }

            MessageDto messageDto = MessageDto.builder()
                    .threadId(threadId)
                    .userId(chatMessage.getSender())
                    .content(chatMessage.getContent())
                    .build();

            Message savedMessage = messageService.sendMessage(messageDto);

            messagingTemplate.convertAndSend(
                    "/chat/thread/" + threadId,
                    savedMessage
            );
        } catch (Exception err) {
            log.error("Error processing message", err);
            messagingTemplate.convertAndSendToUser(
                    chatMessage.getSender().toString(),
                    "/chat/errors",
                    new MessageErrorException("Your message was not sent to the server")
            );
        }
    }

    @MessageMapping("/chat/thread/{threadId}/join")
    public void handleJoin(@DestinationVariable String threadId,
                           @Payload WebSocketMessage joinMessage) {
        log.debug("User {} join thread {}", joinMessage.getSender(), threadId);

        try {
            ChatThread thread = chatThreadService.getThreadById(threadId);

            WebSocketMessage notification = WebSocketMessage.builder()
                    .messageType(MessageType.JOIN)
                    .threadId(threadId)
                    .sender(joinMessage.getSender())
                    .timestamp(LocalDateTime.now())
                    .build();

            messagingTemplate.convertAndSend(
                    "/chat/thread/" + threadId,
                    notification
            );
        } catch (Exception err) {
            log.error("Error processing join", err);

            messagingTemplate.convertAndSendToUser(
                    joinMessage.getSender().toString(),
                    "/chat/errors",
                    new MessageErrorException("An error occurred while joining chat thread")
            );
        }
    }

    @MessageMapping("/chat/thread/{threadId}/leave")
    public void handleLeave(@DestinationVariable String threadId,
                            @Payload WebSocketMessage leaveMessage) {
        log.debug("User {} leaving thread {}", leaveMessage.getSender(),threadId);

        try {
            ChatThread thread = chatThreadService.getThreadById(threadId);

            WebSocketMessage notification = WebSocketMessage.builder()
                    .messageType(MessageType.LEAVE)
                    .threadId(threadId)
                    .sender(leaveMessage.getSender())
                    .timestamp(LocalDateTime.now())
                    .build();

            messagingTemplate.convertAndSend(
                    "/chat/thread/" + threadId,
                    notification
            );
        } catch (Exception err) {
            log.error("Error processing leave", err);

            messagingTemplate.convertAndSendToUser(
                    leaveMessage.getSender().toString(),
                    "/chat/errors",
                    new MessageErrorException("Error leaving chat thread")
            );
        }
    }
}
