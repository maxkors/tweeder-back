package com.maxkors.tweeder.api;

import com.maxkors.tweeder.domain.UserService;
import com.maxkors.tweeder.infrastructure.ProfileDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/profiles/{username}")
    ResponseEntity<ProfileDTO> getProfile(@PathVariable String username) {
        ProfileDTO profile = userService.getProfile(username);

        return ResponseEntity.ok().body(profile);
    }

    @GetMapping("/profiles")
    ResponseEntity<List<com.maxkors.tweeder.security.User>> getAllProfiles() {
        return ResponseEntity.ok().body(userService.getAll());
    }
}
