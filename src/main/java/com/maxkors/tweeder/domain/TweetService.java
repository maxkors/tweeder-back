package com.maxkors.tweeder.domain;

import com.maxkors.tweeder.infrastructure.TweetPlainDTO;
import com.maxkors.tweeder.infrastructure.TweetRepository;
import com.maxkors.tweeder.infrastructure.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.*;

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
    public Optional<Tweet> getTweetById(User principal, Long id) {
        return tweetRepository.getByIdEntirely(id).map(tweet -> {
            List<Long> tweetIds = new ArrayList<>();
            tweetIds.add(tweet.getId());
            tweet.getChildren().forEach(child -> tweetIds.add(child.getId()));

            Set<Long> likedPostIds = this.tweetRepository.getLikedPostIdsFromList(principal.getUsername(), tweetIds);

            if (likedPostIds.contains(tweet.getId())) {
                tweet.setLiked(true);
            }

            for (Tweet children : tweet.getChildren()) {
                if (likedPostIds.contains(children.getId())) {
                    children.setLiked(true);
                }
            }

            return tweet;
        });
    }

    @Transactional
    public List<TweetPlainDTO> getTweetsByUsername(String username) {
        return tweetRepository.getByUsername(username);
    }

    @Transactional
    public Optional<Tweet> createTweet(User principal, String content, Long parentPostId) {
        return this.userRepository.getByUsername(principal.getUsername()).map(user -> {
            Tweet parent = null;

            if (parentPostId != null) {
                parent = this.tweetRepository.getByIdEntirely(parentPostId)
                        .orElseThrow(() -> new IllegalArgumentException("Wrong parent post id"));
                parent.setCommentsCount(parent.getCommentsCount() + 1L);
            }

            Tweet tweet = Tweet.builder()
                    .user(user)
                    .text(content)
                    .likesCount(0L)
                    .commentsCount(0L)
                    .dateTime(LocalDateTime.now())
                    .parent(parent)
                    .build();

            return tweetRepository.save(tweet);
        });
    }

    // TODO: create custom User auth principal class to be able to get userId
    @Transactional
    public void deleteTweetById(User principal, Long tweetId) {
        userRepository.getByUsername(principal.getUsername()).ifPresent(user ->
                tweetRepository.getByIdEntirely(tweetId).ifPresent(tweet -> {
                    if (user.getId().equals(tweet.getUser().getId())) {
                        tweetRepository.delete(tweet);
                    }
                }));
    }

    @Transactional
    public List<TweetPlainDTO> getTweetsFromUserSubscriptions(String username) {
        List<TweetPlainDTO> tweets = tweetRepository.getFromUserSubscriptions(username);
        tweets.sort(Comparator.comparing(TweetPlainDTO::getDateTime).reversed());

        List<Long> tweetIds = new ArrayList<>();

        tweets.forEach(tweet -> tweetIds.add(tweet.getId()));

        Set<Long> likedPostIds = this.tweetRepository.getLikedPostIdsFromList(username, tweetIds);

        for (TweetPlainDTO tweet : tweets) {
            if (likedPostIds.contains(tweet.getId())) {
                tweet.setLiked(true);
            }
        }

        return tweets;
    }
}
