package com.maxkors.tweeder.infrastructure;

import com.maxkors.tweeder.domain.Media;
import com.maxkors.tweeder.security.User;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TweetPlainDTO {

    public TweetPlainDTO(Long id, User user, String text, Long likesCount, Long commentsCount, LocalDateTime dateTime, List<Media> media) {
        this.id = id;
        this.user = user;
        this.text = text;
        this.likesCount = likesCount;
        this.commentsCount = commentsCount;
        this.dateTime = dateTime;
        this.media = media;
    }

    public TweetPlainDTO(Long id, User user, String text, Long likesCount, Long commentsCount, LocalDateTime dateTime, Media media) {
        this.id = id;
        this.user = user;
        this.text = text;
        this.likesCount = likesCount;
        this.commentsCount = commentsCount;
        this.dateTime = dateTime;
        this.media.add(media);
    }

    private Long id;
    private User user;
    private String text;
    private Long likesCount;
    private Long commentsCount;
    private LocalDateTime dateTime;
    private List<Media> media = new ArrayList<>();
    private boolean isLiked;
}
