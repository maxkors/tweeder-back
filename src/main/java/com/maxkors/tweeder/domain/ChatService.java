package com.maxkors.tweeder.domain;

import com.maxkors.tweeder.api.MessagePayload;
import com.maxkors.tweeder.infrastructure.ChatRepository;
import com.maxkors.tweeder.infrastructure.MessageRepository;
import com.maxkors.tweeder.infrastructure.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final ChatRepository chatRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @Transactional
    public List<Chat> getAllByUsername(String username) {
        return this.chatRepository.getAllByUsername(username);
    }

    @Transactional
    public List<Message> getAllChatMessages(Long id) {
        return chatRepository.getAllMessagesByChatId(id);
    }

    @Transactional
    public void sendMessageFromUser(MessagePayload messagePayload, String senderUsername) {
        Long chatId = messagePayload.getChatId();

        this.userRepository.getByUsername(senderUsername).ifPresent(sender -> {
            if (chatId == null || chatId < 1L) {

                this.userRepository.getByUsername(messagePayload.getInvitedUsername()).ifPresent(invited -> {
                    Message message = Message.builder()
                            .sender(sender)
                            .text(messagePayload.getContent())
                            .build();

                    Chat chat = Chat.builder()
                            .participants(List.of(sender, invited))
                            .messages(List.of(message))
                            .build();

                    message.setChat(chat);

                    this.chatRepository.save(chat);
                    this.messageRepository.save(message);

                    chat.getParticipants().forEach(participant -> {
                        this.simpMessagingTemplate.convertAndSendToUser(participant.getUsername(), "/topic/public", message);
                    });
                });

            } else {

                this.chatRepository.findById(messagePayload.getChatId()).ifPresent(chat -> {
                    Message message = Message.builder()
                            .sender(sender)
                            .chat(chat)
                            .text(messagePayload.getContent())
                            .build();

                    Message savedMessage = this.messageRepository.save(message);

                    chat.getParticipants().forEach(participant -> {
                        this.simpMessagingTemplate.convertAndSendToUser(participant.getUsername(), "/topic/public", savedMessage);
                    });
                });
            }
        });
    }
}
