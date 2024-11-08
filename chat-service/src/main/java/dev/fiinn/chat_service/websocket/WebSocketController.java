package dev.fiinn.chat_service.websocket;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {

    @MessageMapping("/send")
    @SendTo("/chat/messages")
    public WebSocketMessage handleMessage(WebSocketMessage message) {
        return message;
    }

    @MessageMapping("/join")
    @SendTo("/chat/join")
    public WebSocketMessage handleJoin(WebSocketMessage message) {
        message.setMessageType(MessageType.JOIN);
        return message;
    }
}
