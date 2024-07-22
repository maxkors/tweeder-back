package com.maxkors.tweeder.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.maxkors.tweeder.api.AvatarUrlSerializer;
import com.maxkors.tweeder.domain.Chat;
import com.maxkors.tweeder.domain.Tweet;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "app_user")
public class User {

    @Id
    @SequenceGenerator(name = "app_user_seq_gen", sequenceName = "app_user_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "app_user_seq_gen")
    private Long id;

    @Column(name = "username")
    private String username;

    @JsonIgnore
    @Column(name = "password")
    private String password;

    @Column(name = "name")
    private String name;

    @JsonIgnore
    @Column(name = "email")
    private String email;

    @JsonSerialize(using = AvatarUrlSerializer.class, as = String.class)
    @Column(name = "avatar_url")
    private String avatarUrl;

    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "app_user__role",
            joinColumns = @JoinColumn(name = "app_user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
//    @Cascade({org.hibernate.annotations.CascadeType.MERGE, })
//    @JsonManagedReference
//    @JsonBackReference
    private Set<Role> roles = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    List<Tweet> tweets = new ArrayList<>();

    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "subscription",
            joinColumns = @JoinColumn(name = "follower_id"),
            inverseJoinColumns = @JoinColumn(name = "subject_id"))
    private List<User> subscriptions = new ArrayList<>();

    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "subscription",
            joinColumns = @JoinColumn(name = "subject_id"),
            inverseJoinColumns = @JoinColumn(name = "follower_id"))
    private List<User> subscribers = new ArrayList<>();

    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "user_like",
            joinColumns = @JoinColumn(name = "app_user_id"),
            inverseJoinColumns = @JoinColumn(name = "tweet_id"))
    private List<Tweet> likedTweets = new ArrayList<>();

    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "bookmark",
            joinColumns = @JoinColumn(name = "app_user_id"),
            inverseJoinColumns = @JoinColumn(name = "tweet_id"))
    private List<Tweet> bookmarks = new ArrayList<>();

    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "app_user__chat",
            joinColumns = @JoinColumn(name = "app_user_id"),
            inverseJoinColumns = @JoinColumn(name = "chat_id"))
    private List<Chat> chats;

    public void addRole(Role role) {
        this.roles.add(role);
    }
}
