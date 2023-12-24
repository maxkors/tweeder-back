package com.maxkors.tweeder.api;

import com.maxkors.tweeder.domain.UserService;
import com.maxkors.tweeder.infrastructure.Profile;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
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
    ResponseEntity<Profile> getProfile(@AuthenticationPrincipal User principal) {
        Profile profile = userService.getProfile(principal.getUsername());

        return ResponseEntity.ok().body(profile);
    }

    @GetMapping("/profiles")
    ResponseEntity<List<com.maxkors.tweeder.security.User>> getAllProfiles() {
        return ResponseEntity.ok().body(userService.getAll());
    }
}
