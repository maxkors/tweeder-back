package com.maxkors.tweeder.domain;

import com.maxkors.tweeder.security.User;

public record UserDataDTO(String username, String name, String email) {

    public static UserDataDTO fromUser(User user) {
        return new UserDataDTO(user.getUsername(), user.getName(), user.getEmail());
    }
}
