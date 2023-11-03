package com.maxkors.tweeder.domain;

import java.time.LocalDateTime;

public class Tweet {
    private Long id;
    private User user;
    private String text;
    private Long comments;
    private Long likes;

    private LocalDateTime dateTime;

    public Tweet(Long id, User user, String text, Long comments, Long likes, LocalDateTime dateTime) {
        this.id = id;
        this.user = user;
        this.text = text;
        this.comments = comments;
        this.likes = likes;
        this.dateTime = dateTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Long getComments() {
        return comments;
    }

    public void setComments(Long comments) {
        this.comments = comments;
    }

    public Long getLikes() {
        return likes;
    }

    public void setLikes(Long likes) {
        this.likes = likes;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }
}
