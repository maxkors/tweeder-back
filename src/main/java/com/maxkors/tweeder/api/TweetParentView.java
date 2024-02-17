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
public class TweetParentView {
    private Long id;
    private User user;
    private String text;
    private Long likesCount;
    private Long commentsCount;
    private LocalDateTime dateTime;
    private boolean isLiked;
    private List<TweetChildView> children;
    private List<Media> media;

    public static TweetParentView from(Tweet tweet, List<TweetChildView> children) {
        return TweetParentView.builder()
                .id(tweet.getId())
                .user(tweet.getUser())
                .text(tweet.getText())
                .likesCount(tweet.getLikesCount())
                .commentsCount(tweet.getCommentsCount())
                .dateTime(tweet.getDateTime())
                .media(tweet.getMedia())
                .children(children)
                .isLiked(tweet.isLiked())
                .build();
    }
}
