package com.maxkors.tweeder.api;

import com.maxkors.tweeder.domain.ChatService;
import com.maxkors.tweeder.domain.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/chats")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @GetMapping
    ResponseEntity<List<ChatWithParticipantsDTO>> getAllUsersChats(@AuthenticationPrincipal User principal) {
        List<ChatWithParticipantsDTO> chats = this.chatService.getAllByUsername(principal.getUsername()).stream()
                .map(ChatWithParticipantsDTO::fromChat).toList();
        return ResponseEntity.ok().body(chats);
    }

    @GetMapping("/{id}/messages")
    ResponseEntity<List<Message>> getChatMessages(@PathVariable("id") Long id) {
        return ResponseEntity.ok().body(this.chatService.getAllChatMessages(id));
    }
}
