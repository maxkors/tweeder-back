package com.maxkors.tweeder.api;

import com.maxkors.tweeder.domain.NewTweetDTO;
import com.maxkors.tweeder.domain.NewUserDataDTO;
import com.maxkors.tweeder.domain.UserDataDTO;
import com.maxkors.tweeder.domain.UserService;
import com.maxkors.tweeder.infrastructure.ProfileCardDTO;
import com.maxkors.tweeder.infrastructure.ProfileDTO;
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
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    ResponseEntity<List<com.maxkors.tweeder.security.User>> getAllProfiles() {
        return ResponseEntity.ok().body(this.userService.getAll());
    }

    @GetMapping("/{username}")
    ResponseEntity<UserDataDTO> getUserData(@PathVariable String username, @AuthenticationPrincipal User principal) {
        if (!username.equals(principal.getUsername())) return ResponseEntity.badRequest().build();

        return this.userService.getUserData(principal.getUsername())
                .map(user -> ResponseEntity.ok(UserDataDTO.fromUser(user)))
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @PutMapping(value = "/{currentUsername}", consumes = "multipart/form-data")
    ResponseEntity<UserDataDTO> editUserData(@PathVariable String currentUsername,
                                             @AuthenticationPrincipal User principal,
                                             @RequestParam(value = "avatar") MultipartFile avatar,
                                             @RequestParam(value = "username") String username,
                                             @RequestParam(value = "name") String name,
                                             @RequestParam(value = "email") String email) {
        if (!currentUsername.equals(principal.getUsername())) return ResponseEntity.badRequest().build();

        return this.userService.editUserData(principal.getUsername(), new NewUserDataDTO(username, name, email, avatar))
                .map(user -> ResponseEntity.ok(UserDataDTO.fromUser(user)))
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @GetMapping("/{username}/profile")
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
