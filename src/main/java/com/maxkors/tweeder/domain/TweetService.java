package com.maxkors.tweeder.domain;

import com.maxkors.tweeder.infrastructure.TweetRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TweetService {

    TweetRepository tweetRepository;

    @Autowired
    public TweetService(TweetRepository tweetRepository) {
        this.tweetRepository = tweetRepository;
    }

    @Transactional
    public List<Tweet> getAllTweets() {
        return tweetRepository.getAll();
    }

    @Transactional
    public Optional<Tweet> getTweetById(Long id) {
        return Optional.of(tweetRepository.getById(id));
    }
}
