package com.maxkors.tweeder.domain;

import com.maxkors.tweeder.infrastructure.CommentRepository;
import com.maxkors.tweeder.infrastructure.TweetRepository;
import com.maxkors.tweeder.infrastructure.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final TweetRepository tweetRepository;
    private final UserRepository userRepository;

    @Transactional
    public Optional<Comment> getById(Long id) {
        return this.commentRepository.findById(id);
    }

    @Transactional
    public List<Comment> getCommentsByUsername(String username) {
        return this.commentRepository.getByAllByUsername(username);
    }

    // TODO: optimize queries and create additional Repo methods so as not to receive unnecessary data
    @Transactional
    public void createComment(User principal, String content, Long tweetId) {
        this.userRepository.getByUsername(principal.getUsername()).ifPresent(user ->
                this.tweetRepository.getByIdEntirely(tweetId).ifPresent(tweet -> {
                            Comment comment = Comment.builder()
                                    .tweet(tweet).user(user).text(content).likes(0L).dateTime(LocalDateTime.now()).build();
                            this.commentRepository.save(comment);

                            tweet.setCommentsCount(tweet.getCommentsCount() + 1L);
                        }
                ));
    }

    @Transactional
    public void deleteCommentById(User principal, Long commentId) {
        this.userRepository.getByUsername(principal.getUsername()).ifPresent(user ->
                this.commentRepository.findById(commentId).ifPresent(comment -> {
                    if (user.getId().equals(comment.getUser().getId())) {
                        this.commentRepository.delete(comment);

                        Tweet tweet = comment.getTweet();
                        tweet.setCommentsCount(tweet.getCommentsCount() - 1L);
                    }
                }));
    }
}
