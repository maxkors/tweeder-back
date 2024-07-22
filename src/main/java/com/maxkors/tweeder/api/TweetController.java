package com.maxkors.tweeder.api;

import com.maxkors.tweeder.domain.NewTweetDTO;
import com.maxkors.tweeder.domain.Tweet;
import com.maxkors.tweeder.domain.TweetService;
import com.maxkors.tweeder.infrastructure.TweetPlainDTO;

import io.jsonwebtoken.lang.Collections;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/tweets")
@RequiredArgsConstructor
public class TweetController {

    private final TweetService tweetService;

    @GetMapping
    ResponseEntity<List<TweetChildView>> getAllTweets() {
        List<Tweet> allTweets = tweetService.getAllTweets();
        return ResponseEntity.ok().body(allTweets.stream().map(TweetChildView::from).toList());
    }

    @PostMapping(consumes = "multipart/form-data")
    ResponseEntity<TweetParentView> createTweet(@AuthenticationPrincipal User principal,
                                                @RequestParam(value = "media") MultipartFile media,
                                                @RequestParam(value = "parentPostId") @Nullable Long parentPostId,
                                                @RequestParam(value = "text") String text) {
                                                    System.out.println(media);
        return tweetService.createTweet(principal, new NewTweetDTO(text, parentPostId, media.isEmpty() ? Collections.emptyList() : List.of(media)))
                .map(tweet -> ResponseEntity.ok().body(TweetParentView.from(tweet, null)))
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @GetMapping("/{id}")
    ResponseEntity<TweetParentView> getTweetById(@AuthenticationPrincipal User principal, @PathVariable("id") Long id) {
        return tweetService.getTweetById(principal, id)
                .map((tweet) -> {
                    List<TweetChildView> children = tweet.getChildren().stream().map(TweetChildView::from).toList();
                    TweetParentView parent = TweetParentView.from(tweet, children);
                    return ResponseEntity.ok().body(parent);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    ResponseEntity<String> deleteTweetById(@AuthenticationPrincipal User principal, @PathVariable("id") Long id) {
        tweetService.deleteTweetById(principal, id);
        return ResponseEntity.ok().body("Tweet deleted");
    }

    @GetMapping("/users/{username}")
    ResponseEntity<List<TweetChildView>> getTweetsByUsername(@PathVariable("username") String username) {
        List<Tweet> tweets = tweetService.getTweetsByUsername(username);
        return ResponseEntity.ok().body(tweets.stream().map(TweetChildView::from).toList());
    }

    @GetMapping("/users/{username}/liked")
    ResponseEntity<List<TweetChildView>> getLikedTweetsByUsername(@AuthenticationPrincipal User principal, @PathVariable("username") String username) {
        List<Tweet> tweets = tweetService.getLikedPostsByUsername(username, principal);
        return ResponseEntity.ok().body(tweets.stream().map(TweetChildView::from).toList());
    }

    @GetMapping("/feed")
    ResponseEntity<List<TweetChildView>> getTweetsFromUserSubscriptions(@AuthenticationPrincipal User principal) {
        List<Tweet> tweets = tweetService.getTweetsFromUserSubscriptions(principal.getUsername());
        return ResponseEntity.ok().body(tweets.stream().map(TweetChildView::from).toList());
    }

    @PostMapping("/{id}/like")
    ResponseEntity<?> addLike(@AuthenticationPrincipal User principal, @PathVariable("id") Long id) {
        this.tweetService.addLike(id, principal.getUsername());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/like")
    ResponseEntity<?> removeLike(@AuthenticationPrincipal User principal, @PathVariable("id") Long id) {
        this.tweetService.removeLike(id, principal.getUsername());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/bookmark")
    ResponseEntity<?> addBookMark(@AuthenticationPrincipal User principal, @PathVariable("id") Long id) {
        this.tweetService.addBookmark(id, principal.getUsername());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/bookmark")
    ResponseEntity<?> removeBookMark(@AuthenticationPrincipal User principal, @PathVariable("id") Long id) {
        this.tweetService.removeBookmark(id, principal.getUsername());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/bookmarked")
    ResponseEntity<List<TweetChildView>> getBookmarked(@AuthenticationPrincipal User principal) {
        List<Tweet> tweets = this.tweetService.getAllBookmarkedByUser(principal.getUsername());
        return ResponseEntity.ok().body(tweets.stream().map(TweetChildView::from).toList());
    }

    @GetMapping("/recommended")
    ResponseEntity<List<TweetChildView>> getRecommended(@AuthenticationPrincipal User principal) {
        List<Tweet> tweets = this.tweetService.getRecommendedPosts(principal.getUsername());
        return ResponseEntity.ok().body(tweets.stream().map(TweetChildView::from).toList());
    }

    private record NewTweetData(String text, @Nullable Long parentPostId) {}
}
