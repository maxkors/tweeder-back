package com.maxkors.tweeder.api;

import com.maxkors.tweeder.security.AuthenticationResponse;
import com.maxkors.tweeder.security.AuthenticationService;
import com.maxkors.tweeder.security.SigninRequest;
import com.maxkors.tweeder.security.SignupRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(value = "http://localhost:3000/")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/signup")
    public ResponseEntity<AuthenticationResponse> signup(@RequestBody SignupRequest signupRequest) {
        return ResponseEntity.ok().body(authenticationService.signup(signupRequest));
    }

    @PostMapping("/signin")
    public ResponseEntity<AuthenticationResponse> signin(@RequestBody SigninRequest signinRequest) {
        return ResponseEntity.ok().body(authenticationService.signin(signinRequest));
    }
}
