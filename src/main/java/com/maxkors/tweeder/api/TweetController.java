package com.maxkors.tweeder.api;

import com.maxkors.tweeder.domain.Tweet;
import com.maxkors.tweeder.domain.TweetService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/{id}")
    ResponseEntity<Tweet> getTweetById(@PathVariable("id") Long id) {
        return tweetService.getTweetById(id)
                .map((tweet) -> ResponseEntity.ok().body(tweet))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
