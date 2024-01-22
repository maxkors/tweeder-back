package com.maxkors.tweeder.api;

import com.maxkors.tweeder.security.User;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class TweetParentView {
    private Long id;
    private User user;
    private String text;
    private Long likesCount;
    private Long commentsCount;
    private LocalDateTime dateTime;
    private boolean isLiked;
    private List<TweetChildView> children;
}
