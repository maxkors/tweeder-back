package com.maxkors.tweeder.api;

import com.maxkors.tweeder.domain.Tweet;
import com.maxkors.tweeder.domain.TweetService;
import com.maxkors.tweeder.infrastructure.TweetPlainDTO;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tweets")
@RequiredArgsConstructor
public class TweetController {

    private final TweetService tweetService;

    @GetMapping
    List<TweetPlainDTO> getAllTweets() {
        return tweetService.getAllTweets();
    }

    @PostMapping
    ResponseEntity<TweetParentView> createTweet(@AuthenticationPrincipal User principal, @RequestBody TweetRequest tweetRequest) {
        return tweetService.createTweet(principal, tweetRequest.text, tweetRequest.parentPostId)
                .map(tweet -> {

                    TweetParentView tweetParentView = TweetParentView.builder()
                            .id(tweet.getId())
                            .user(tweet.getUser())
                            .text(tweet.getText())
                            .likesCount(tweet.getLikesCount())
                            .commentsCount(tweet.getCommentsCount())
                            .dateTime(tweet.getDateTime())
                            .isLiked(tweet.isLiked())
                            .children(null)
                            .build();

                    return ResponseEntity.ok().body(tweetParentView);
                })
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @GetMapping("/{id}")
    ResponseEntity<TweetParentView> getTweetById(@AuthenticationPrincipal User principal, @PathVariable("id") Long id) {
//        return tweetService.getTweetById(id)
//                .map((tweet) -> ResponseEntity.ok().body(tweet))
//                .orElseGet(() -> ResponseEntity.notFound().build());

        return tweetService.getTweetById(principal, id)
                .map((tweet) -> {
                    List<TweetChildView> children = tweet.getChildren().stream().map(child -> TweetChildView.builder()
                            .id(child.getId())
                            .user(child.getUser())
                            .text(child.getText())
                            .likesCount(child.getLikesCount())
                            .commentsCount(child.getCommentsCount())
                            .dateTime(child.getDateTime())
                            .isLiked(child.isLiked())
                            .build()).toList();

                    TweetParentView parent = TweetParentView.builder()
                            .id(tweet.getId())
                            .user(tweet.getUser())
                            .text(tweet.getText())
                            .likesCount(tweet.getLikesCount())
                            .commentsCount(tweet.getCommentsCount())
                            .dateTime(tweet.getDateTime())
                            .isLiked(tweet.isLiked())
                            .children(children)
                            .build();

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
    ResponseEntity<List<TweetPlainDTO>> getTweetsByUsername(@PathVariable("username") String username) {
        return ResponseEntity.ok().body(tweetService.getTweetsByUsername(username));
    }

    @GetMapping("/users/{username}/liked")
    ResponseEntity<List<TweetPlainDTO>> getLikedTweetsByUsername(@AuthenticationPrincipal User principal, @PathVariable("username") String username) {
        return ResponseEntity.ok().body(tweetService.getLikedPostsByUsername(username, principal));
    }

    @GetMapping("/feed")
    ResponseEntity<List<TweetPlainDTO>> getTweetsFromUserSubscriptions(@AuthenticationPrincipal User principal) {
        return ResponseEntity.ok().body(tweetService.getTweetsFromUserSubscriptions(principal.getUsername()));
    }

    @PostMapping("/{id}/like")
    ResponseEntity<?> addLike(@AuthenticationPrincipal User principal, @PathVariable("id") Long id) {
        return this.tweetService.addLike(id, principal.getUsername())
                .filter(aBoolean -> aBoolean).map(aBoolean -> ResponseEntity.ok().build())
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @DeleteMapping("/{id}/like")
    ResponseEntity<?> removeLike(@AuthenticationPrincipal User principal, @PathVariable("id") Long id) {
        return this.tweetService.removeLike(id, principal.getUsername())
                .filter(aBoolean -> aBoolean).map(aBoolean -> ResponseEntity.ok().build())
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    record TweetRequest(String text, @Nullable Long parentPostId) {
    }
}
