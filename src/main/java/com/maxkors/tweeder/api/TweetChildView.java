package com.maxkors.tweeder.api;

import com.maxkors.tweeder.domain.Media;
import com.maxkors.tweeder.domain.Tweet;
import com.maxkors.tweeder.security.User;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class TweetChildView {
    private Long id;
    private User user;
    private String text;
    private Long likesCount;
    private Long commentsCount;
    private LocalDateTime dateTime;
    private List<Media> media;
    private boolean isLiked;

    public static TweetChildView from(Tweet tweet) {
        return TweetChildView.builder()
                .id(tweet.getId())
                .user(tweet.getUser())
                .text(tweet.getText())
                .likesCount(tweet.getLikesCount())
                .commentsCount(tweet.getCommentsCount())
                .dateTime(tweet.getDateTime())
                .media(tweet.getMedia())
                .isLiked(tweet.isLiked())
                .build();
    }
}
