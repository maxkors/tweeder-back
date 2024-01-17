package com.maxkors.tweeder.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.maxkors.tweeder.security.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "comment")
public class Comment {

    @Id
    @SequenceGenerator(name = "comment_seq_gen", sequenceName = "comment_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "comment_seq_gen")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tweet_id")
    @JsonBackReference
    private Tweet tweet;

    @ManyToOne
    @JoinColumn(name = "app_user_id")
    private User user;

    @Column(name = "text")
    private String text;

    @Column(name = "likes_count")
    private Long likesCount;

    @Column(name = "date_time")
    private LocalDateTime dateTime;
}
