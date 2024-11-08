package dev.fiinn.chat_service.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class UserSessionInfo {
    private UUID userId;
    private String threadId;
}
