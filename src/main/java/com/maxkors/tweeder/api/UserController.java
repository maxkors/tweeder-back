package com.maxkors.tweeder.api;

import com.maxkors.tweeder.domain.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin(value = "http://localhost:3000/")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/profile")
    ResponseEntity<ProfileWithSubscriptions> getProfile(@AuthenticationPrincipal User principal) {
        com.maxkors.tweeder.security.User user = userService
                .getUserByUsername(principal.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        List<com.maxkors.tweeder.security.User> subscriptions = userService.getSubscriptions(user.getUsername());

        return ResponseEntity.ok().body(new ProfileWithSubscriptions(user.getUsername(), user.getName(), user.getEmail(), subscriptions));
    }

    @GetMapping("/profiles")
    ResponseEntity<List<Profile>> getAllProfiles() {
        return ResponseEntity.ok().body(userService
                .getAll()
                .stream()
                .map(user -> new Profile(user.getUsername(), user.getName(), user.getEmail()))
                .toList());
    }

    record Profile(String username, String name, String email) {
    }

    record ProfileWithSubscriptions(String username, String name, String email, List<com.maxkors.tweeder.security.User> subscriptions) {
    }
}
