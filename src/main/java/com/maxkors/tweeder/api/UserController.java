package com.maxkors.tweeder.api;

import com.maxkors.tweeder.domain.UserService;
import com.maxkors.tweeder.infrastructure.ProfileCardDTO;
import com.maxkors.tweeder.infrastructure.ProfileDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    ResponseEntity<List<com.maxkors.tweeder.security.User>> getAllProfiles() {
        return ResponseEntity.ok().body(this.userService.getAll());
    }

    @GetMapping("/{username}")
    ResponseEntity<ProfileDTO> getProfile(@PathVariable String username, @AuthenticationPrincipal User principal) {
        ProfileDTO profile = this.userService.getProfile(username, principal);

        return ResponseEntity.ok().body(profile);
    }

    @PostMapping("/{username}/follow")
    ResponseEntity<?> followUser(@PathVariable("username") String username, @AuthenticationPrincipal User principal) {
        this.userService.addFollower(principal.getUsername(), username);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{username}/follow")
    ResponseEntity<?> unfollowUser(@PathVariable("username") String username, @AuthenticationPrincipal User principal) {
        this.userService.removeFollower(principal.getUsername(), username);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/{username}/subscriptions")
    ResponseEntity<List<com.maxkors.tweeder.security.User>> getSubscriptions(@PathVariable("username") String username) {
        return ResponseEntity.ok().body(this.userService.getSubscriptions(username));
    }

    @GetMapping("/{username}/subscribers")
    ResponseEntity<List<com.maxkors.tweeder.security.User>> getSubscribers(@PathVariable("username") String username) {
        return ResponseEntity.ok().body(this.userService.getSubscribers(username));
    }

    @GetMapping("/search")
    ResponseEntity<List<ProfileCardDTO>> findMatchingProfiles(@RequestParam("name") String name) {
        return ResponseEntity.ok().body(this.userService.getMatchingProfiles(name));
    }
}
