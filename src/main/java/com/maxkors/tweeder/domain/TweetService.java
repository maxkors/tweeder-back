package com.maxkors.tweeder.domain;

import com.maxkors.tweeder.infrastructure.TweetPlainDTO;
import com.maxkors.tweeder.infrastructure.TweetRepository;
import com.maxkors.tweeder.infrastructure.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class TweetService {

    private final TweetRepository tweetRepository;
    private final UserRepository userRepository;

    @Transactional
    public List<Tweet> getAllTweets() {
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
    public List<Tweet> getTweetsByUsername(String username) {
//        return tweetRepository.getByUsername(username);

        List<Tweet> tweets = tweetRepository.getByUsernameAll(username);
        tweets.sort(Comparator.comparing(Tweet::getDateTime).reversed());

        List<Long> tweetIds = new ArrayList<>();

        tweets.forEach(tweet -> tweetIds.add(tweet.getId()));

        Set<Long> likedPostIds = this.tweetRepository.getLikedPostIdsFromList(username, tweetIds);

        for (Tweet tweet : tweets) {
            if (likedPostIds.contains(tweet.getId())) {
                tweet.setLiked(true);
            }
        }

        return tweets;
    }

    @Transactional
    public List<Tweet> getLikedPostsByUsername(String username, User principal) {
        List<Tweet> likedTweets = tweetRepository.getLikedByUsername(username);

        if (principal.getUsername().equals(username)) {
            likedTweets.forEach(tweet -> tweet.setLiked(true));
            return likedTweets.reversed();
        }

        List<Long> tweetIds = new ArrayList<>();
        likedTweets.forEach(tweet -> tweetIds.add(tweet.getId()));

        Set<Long> principalLikedPostIds = this.tweetRepository.getLikedPostIdsFromList(principal.getUsername(), tweetIds);

        for (Tweet tweet : likedTweets) {
            if (principalLikedPostIds.contains(tweet.getId())) {
                tweet.setLiked(true);
            }
        }


        return likedTweets.reversed();
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
    public List<Tweet> getTweetsFromUserSubscriptions(String username) {
        List<Tweet> tweets = tweetRepository.getFromUserSubscriptions(username);
        tweets.addAll(tweetRepository.getByUsernameParents(username));

        tweets.sort(Comparator.comparing(Tweet::getDateTime).reversed());

        List<Long> tweetIds = new ArrayList<>();

        tweets.forEach(tweet -> tweetIds.add(tweet.getId()));

        Set<Long> likedPostIds = this.tweetRepository.getLikedPostIdsFromList(username, tweetIds);

        for (Tweet tweet : tweets) {
            if (likedPostIds.contains(tweet.getId())) {
                tweet.setLiked(true);
            }
        }

        return tweets;
    }

    @Transactional
    public void addLike(Long postId, String username) {
        this.userRepository.getByUsername(username).ifPresent(user ->
                this.tweetRepository.getByIdWithLikes(postId).ifPresent(tweet -> {
                    tweet.getLikes().add(user);
                    tweet.setLikesCount(tweet.getLikesCount() + 1L);
                }));
    }

    @Transactional
    public void removeLike(Long postId, String username) {
        this.userRepository.getByUsername(username).ifPresent(user ->
                this.tweetRepository.getByIdWithLikes(postId).ifPresent(tweet -> {
                    tweet.getLikes().remove(user);
                    tweet.setLikesCount(tweet.getLikesCount() - 1L);
                }));
    }
}
