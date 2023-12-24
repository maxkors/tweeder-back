package com.maxkors.tweeder.api;

import com.maxkors.tweeder.domain.Tweet;
import com.maxkors.tweeder.domain.TweetService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/tweets")
@CrossOrigin("http://localhost:3000/")
@RequiredArgsConstructor
public class TweetController {

    private final TweetService tweetService;

    @GetMapping
    List<Tweet> getAllTweets() {
        return tweetService.getAllTweets();
    }

    @PostMapping
    ResponseEntity<String> createTweet(@AuthenticationPrincipal User principal, @RequestBody String content) {
        tweetService.createTweet(principal, content);
        return ResponseEntity.ok().body("Tweet created");
    }

    @GetMapping("/{id}")
    ResponseEntity<Tweet> getTweetById(@PathVariable("id") Long id) {
        return tweetService.getTweetById(id)
                .map((tweet) -> ResponseEntity.ok().body(tweet))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/users/{username}")
    ResponseEntity<List<TweetWithoutCommentsDTO>> getTweetsByUsername(@PathVariable("username") String username) {
        List<Tweet> tweets = tweetService.getTweetsByUsername(username);

        List<TweetWithoutCommentsDTO> tweetWithoutComments = tweets.stream()
                .map((t -> new TweetWithoutCommentsDTO(t.getId(), t.getUser(), t.getText(), t.getLikes(), t.getDateTime()))).toList();

        return ResponseEntity.ok().body(tweetWithoutComments);
    }

    record TweetWithoutCommentsDTO(Long id, com.maxkors.tweeder.security.User user, String text, Long likes, LocalDateTime dateTime) {}
}
