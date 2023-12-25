package com.maxkors.tweeder.domain;

import com.maxkors.tweeder.infrastructure.TweetPlainDTO;
import com.maxkors.tweeder.infrastructure.TweetRepository;
import com.maxkors.tweeder.infrastructure.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TweetService {

    private final TweetRepository tweetRepository;
    private final UserRepository userRepository;

    @Transactional
    public List<TweetPlainDTO> getAllTweets() {
        return tweetRepository.getAll();
    }

    @Transactional
    public Optional<Tweet> getTweetById(Long id) {
        return tweetRepository.getByIdEntirely(id);
    }

    @Transactional
    public List<Tweet> getTweetsByUsername(String username) {
        return tweetRepository.getTweetsByUsername(username);
    }

    @Transactional
    public void createTweet(User principal, String content) {
        userRepository.getByUsername(principal.getUsername()).ifPresent(user -> {
            Tweet tweet = Tweet.builder().user(user).text(content).likes(0L).dateTime(LocalDateTime.now()).build();
            tweetRepository.save(tweet);
        });
    }

    // TODO: create custom User auth principal class to be able to get userId
    @Transactional
    public void deleteTweetById(User principal, Long tweetId) {
        userRepository.getByUsername(principal.getUsername()).ifPresent(user -> {
            tweetRepository.getByIdEntirely(tweetId).ifPresent(tweet -> {
                if (user.getId().equals(tweet.getUser().getId())) {
                    tweetRepository.delete(tweet);
                }
            });
        });
    }
}
