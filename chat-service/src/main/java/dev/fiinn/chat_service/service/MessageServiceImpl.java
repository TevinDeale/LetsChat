package dev.fiinn.chat_service.service;

import dev.fiinn.chat_service.dto.MessageDto;
import dev.fiinn.chat_service.enums.MessageStatus;
import dev.fiinn.chat_service.enums.Role;
import dev.fiinn.chat_service.exception.MessageNotFoundException;
import dev.fiinn.chat_service.exception.ParticipantNotFoundException;
import dev.fiinn.chat_service.exception.RequiredPermissionsException;
import dev.fiinn.chat_service.model.ChatThread;
import dev.fiinn.chat_service.model.Message;
import dev.fiinn.chat_service.queue.ChatMessageEvent;
import dev.fiinn.chat_service.queue.ChatMessageProducer;
import dev.fiinn.chat_service.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService{
    private final MessageRepository messageRepository;
    private final ChatThreadService chatThreadService;
    private final ChatMessageProducer messageProducer;

    private final MessageStatus sent = MessageStatus.SENT;
    private final MessageStatus read = MessageStatus.READ;
    private final MessageStatus delivered = MessageStatus.DELIVERED;

    @Override
    public Message sendMessage(MessageDto messageDto) {
        if (!validUserIsInThread(messageDto.getThreadId(), messageDto.getUserId())) {
            throw new ParticipantNotFoundException("User is not apart of thread: " + messageDto.getThreadId());
        }

        ChatThread chatThread = chatThreadService.getThreadById(messageDto.getThreadId());
        chatThread.setLastMessageTimestamp(LocalDateTime.now());

        Message message = Message.builder()
                .message(messageDto.getContent())
                .threadId(messageDto.getThreadId())
                .userId(messageDto.getUserId())
                .build();

        message =  messageRepository.save(message);

        ChatMessageEvent event = new ChatMessageEvent(
                message.getMessageId(),
                message.getThreadId(),
                message.getUserId(),
                message.getMessage(),
                message.getTimestamp()
        );

        messageProducer.sendMessage(event);
        return message;
    }

    @Override
    public List<Message> getMessages(String threadId) {
        return messageRepository.findAllByThreadId(threadId);
    }

    @Override
    public List<Message> getUnreadMessages(String threadId, UUID userId) {
        return messageRepository.findAllByUserIdAndMessageStatusAndThreadId(userId, delivered, threadId);
    }

    @Override
    public boolean deleteMessage(String messageId, UUID userId) {
        Message messageToDelete = findById(messageId);

        ChatThread chatThread = chatThreadService.getThreadById(messageToDelete.getThreadId());

        boolean isMessageOwner = userId.equals(messageToDelete.getUserId());
        boolean isThreadOwner = chatThread.getParticipantRole(userId).equals(Role.OWNER);
        boolean isThreadModerator = chatThread.getParticipantRole(userId).equals(Role.MODERATOR);

        if (isMessageOwner || isThreadOwner || isThreadModerator) {
            messageRepository.delete(messageToDelete);
            return true;
        } else {
            throw new RequiredPermissionsException("You do not have permission to delete this message");
        }
    }

    @Override
    public boolean markMessageAsRead(String messageId, UUID userId) {
        Message message = findById(messageId);

        message.setMessageStatus(read);
        message.setReadAt(LocalDateTime.now());
        message.addToReadBy(userId);

        messageRepository.save(message);
        return true;
    }

    @Override
    public boolean markMessageDelivered(String messageId, UUID userId) {
        Message message = findById(messageId);

        message.setMessageStatus(delivered);
        message.addToDeliveredTo(userId);

        messageRepository.save(message);
        return true;
    }

    @Override
    public boolean markMessageAsSent(String messageId, UUID userId) {
        Message message = findById(messageId);

        message.setMessageStatus(sent);
        return true;
    }

    @Override
    public Integer getUnreadMessageCount(String threadId, UUID userId) {
        List<Message> unreadMessages = messageRepository.findAllByUserIdAndMessageStatusAndThreadId(userId, delivered, threadId);
        return unreadMessages.size();
    }

    @Override
    public void makeMessageAsFailed(String messageId, String error) {
        Message message = findById(messageId);
        message.setFailed(true);
        message.setErrorMessage("Message was not sent");
        messageRepository.save(message);
    }

    @Override
    public void retryFailedMessage(String messageId) {
        Message message = findById(messageId);

        if (message.isFailed()) {
            ChatMessageEvent event = new ChatMessageEvent(
                    message.getMessageId(),
                    message.getThreadId(),
                    message.getUserId(),
                    message.getMessage(),
                    message.getTimestamp()
            );

            messageProducer.sendMessage(event);

            message.setFailed(false);
            message.setErrorMessage(null);
            messageRepository.save(message);
        }
    }

    private boolean validUserIsInThread(String threadId, UUID userId) {
        return chatThreadService.isUserInThread(threadId, userId);
    }

    public Message findById(String messageId) {

        return messageRepository.findById(messageId)
                .orElseThrow(() -> new MessageNotFoundException("Message with ID: " + messageId + "was not found"));
    }
}
