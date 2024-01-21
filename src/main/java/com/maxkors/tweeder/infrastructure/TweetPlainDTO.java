package com.maxkors.tweeder.infrastructure;

import com.maxkors.tweeder.security.User;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TweetPlainDTO {

    public TweetPlainDTO(Long id, User user, String text, Long likesCount, Long commentsCount, LocalDateTime dateTime) {
        this.id = id;
        this.user = user;
        this.text = text;
        this.likesCount = likesCount;
        this.commentsCount = commentsCount;
        this.dateTime = dateTime;
    }

    private Long id;
    private User user;
    private String text;
    private Long likesCount;
    private Long commentsCount;
    private LocalDateTime dateTime;
    private boolean isLiked;
}
