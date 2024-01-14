package com.maxkors.tweeder.infrastructure;

import com.maxkors.tweeder.security.User;
import lombok.*;

import java.time.LocalDateTime;

public interface TweetPlainDTO {
    Long getId();
    String getName();
    String getUsername();
    String getText();
    Long getLikes();
    LocalDateTime getDateTime();
    Long getCommentsCount();
}
