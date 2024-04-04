package com.maxkors.tweeder.api;

import com.maxkors.tweeder.domain.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ChatControllerWS {

    private final ChatService chatService;

    @MessageMapping("/chat/sendMessage")
    @SendToUser("/topic/public")
    public void sendMessage(@Payload MessagePayload message, Principal principal) {
        log.info("{}: {}", principal.getName(), message.getContent());
        this.chatService.sendMessageFromUser(message, principal.getName());
    }

//    @MessageMapping("/chat/addUser")
//    @SendTo("/topic/public")
//    public ChatMessage addUser(@Payload ChatMessage message, SimpMessageHeaderAccessor headerAccessor) {
//        headerAccessor.getSessionAttributes().put("user", message.getSender());
//        return message;
//    }
}
