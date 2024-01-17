package com.maxkors.tweeder.api;

import com.maxkors.tweeder.domain.Comment;
import com.maxkors.tweeder.domain.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    ResponseEntity<Comment> createComment(@AuthenticationPrincipal User principal, @RequestBody CommentRequest commentRequest) {
        return commentService.createComment(principal, commentRequest.content, commentRequest.tweetId)
                .map(comment -> ResponseEntity.ok().body(comment))
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Comment> getCommentById(@PathVariable("id") Long id) {
        return commentService.getById(id)
                .map(comment -> ResponseEntity.ok().body(comment))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    ResponseEntity<String> deleteCommentById(@AuthenticationPrincipal User principal, @PathVariable("id") Long id) {
        commentService.deleteCommentById(principal, id);
        return ResponseEntity.ok().body("Comment deleted");
    }

    @GetMapping("/users/{username}")
    ResponseEntity<List<Comment>> getCommentsByUsername(@PathVariable("username") String username) {
        return ResponseEntity.ok().body(commentService.getCommentsByUsername(username));
    }

    record CommentRequest(String content, Long tweetId) {
    }
}
