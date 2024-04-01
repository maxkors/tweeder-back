package com.maxkors.tweeder.config;

import com.maxkors.tweeder.api.MessagePayload;
import com.maxkors.tweeder.api.MessageType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
@RequiredArgsConstructor
@Slf4j
public class WeSocketEventListener {

    private final SimpMessageSendingOperations simpMessageSendingOperations;

//    @EventListener
//    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
//        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
//        String username = (String) headerAccessor.getSessionAttributes().get("username");
//
//        if (username != null) {
//            log.info("User disconnected: {}", username);
//
//            var chatMessage = MessagePayload.builder()
//                    .type(MessageType.LEAVE)
//                    .sender(username)
//                    .build();
//
//            simpMessageSendingOperations.convertAndSend("/topic/public", chatMessage);
//        }
//    }
}
