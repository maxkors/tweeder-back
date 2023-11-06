package com.maxkors.tweeder.api;

import com.maxkors.tweeder.domain.Tweet;
import com.maxkors.tweeder.domain.TweetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@CrossOrigin("http://localhost:3000/")
@RequestMapping("/tweets")
public class TweetController {

    TweetService tweetService;

    @Autowired
    public TweetController(TweetService tweetService) {
        this.tweetService = tweetService;
    }

    @GetMapping
    List<Tweet> getAllTweets() {
        return tweetService.getAllTweets();
    }

//    @GetMapping("/{id}")
//    Tweet getTweetById(@PathVariable("id") Long id) {
//        return tweets.get(id.intValue() - 1);
//    }
}
