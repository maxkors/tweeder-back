package com.maxkors.tweeder.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.maxkors.tweeder.security.Role;
import com.maxkors.tweeder.security.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tweet")
public class Tweet {

    @Id
    @SequenceGenerator(name = "tweet_seq_gen", sequenceName = "tweet_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tweet_seq_gen")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "app_user_id")
    private User user;

    @OneToMany(mappedBy = "tweet", orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @Column(name = "text")
    private String text;

    @Column(name = "likes_count")
    private Long likesCount;

    @Column(name = "comments_count")
    private Long commentsCount;

    @Column(name = "date_time")
    private LocalDateTime dateTime;

    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "user_like",
            joinColumns = @JoinColumn(name = "tweet_id"),
            inverseJoinColumns = @JoinColumn(name = "app_user_id"))
    private List<User> likes = new ArrayList<>();
}
