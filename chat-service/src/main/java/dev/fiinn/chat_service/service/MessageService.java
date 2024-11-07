package dev.fiinn.chat_service.service;

import dev.fiinn.chat_service.dto.MessageDto;
import dev.fiinn.chat_service.enums.MessageStatus;
import dev.fiinn.chat_service.model.Message;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    Message sendMessage(MessageDto messageDto);
    List<Message> getMessages(String threadId);
    List<Message> getUnreadMessages(String threadId, UUID userId);
    boolean deleteMessage(String messageId, UUID userId);
    boolean markMessageAsRead(String messageId, UUID userId);
    boolean markMessageDelivered(String messageId, UUID userId);
    boolean markMessageAsSent(String messageId, UUID userId);
    Integer getUnreadMessageCount(String threadId, UUID userId);
    Message findById(String messageId);
    public void makeMessageAsFailed(String messageId, String error);
    public void retryFailedMessage(String messageId);
}
