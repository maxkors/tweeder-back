package com.maxkors.tweeder.api;

import com.maxkors.tweeder.domain.Tweet;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@CrossOrigin("http://localhost:3000/")
@RequestMapping("/tweets")
public class TweetController {

    List<Tweet> tweets;

    public TweetController() {
        var u1 = new User(1L, "mxms", "Maximus");
        var u2 = new User(2L, "4lex", "Alex");
        var u3 = new User(3L, "chezz", "Chester");

        this.tweets = List.of(new Tweet(1L, u1, "Hi there", 3L, 25L, LocalDateTime.now().minusHours(1)),
                new Tweet(2L, u2, "Can someone explain to me what gravity is?", 0L, 10L, LocalDateTime.now().minusHours(2)),
                new Tweet(3L, u3, "I have something to tell you...", 33L, 195L, LocalDateTime.now().minusHours(4)));
    }

    @GetMapping
    List<Tweet> getAllTweets() {
        return tweets;
    }

    @GetMapping("/{id}")
    Tweet getTweetById(@PathVariable("id") Long id) {
        return tweets.get(id.intValue() - 1);
    }
}
