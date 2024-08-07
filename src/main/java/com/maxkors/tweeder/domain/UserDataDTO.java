package com.maxkors.tweeder.domain;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.maxkors.tweeder.api.AvatarUrlSerializer;
import com.maxkors.tweeder.security.User;

public record UserDataDTO(String username, String name, String email, @JsonSerialize(using = AvatarUrlSerializer.class, as = String.class) String avatarUrl) {

    public static UserDataDTO fromUser(User user) {
        return new UserDataDTO(user.getUsername(), user.getName(), user.getEmail(), user.getAvatarUrl());
    }
}
