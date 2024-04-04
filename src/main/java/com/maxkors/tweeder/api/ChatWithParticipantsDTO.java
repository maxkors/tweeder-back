package com.maxkors.tweeder.api;

import com.maxkors.tweeder.domain.Chat;
import com.maxkors.tweeder.security.User;

import java.util.List;

public record ChatWithParticipantsDTO(Long id, List<User> participants) {

    public static ChatWithParticipantsDTO fromChat(Chat chat) {
        return new ChatWithParticipantsDTO(chat.getId(), chat.getParticipants());
    }
}
