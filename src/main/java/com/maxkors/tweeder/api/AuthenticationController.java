package com.maxkors.tweeder.api;

import com.maxkors.tweeder.security.AuthenticationResponse;
import com.maxkors.tweeder.security.AuthenticationService;
import com.maxkors.tweeder.security.SigninRequest;
import com.maxkors.tweeder.security.SignupRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
//@CrossOrigin( allowCredentials = "true", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.OPTIONS}, allowedHeaders = "Origin, Content-Type, Accept", origins = "http://localhost:3000")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/signup")
    public ResponseEntity<AuthenticationResponse> signup(@RequestBody SignupRequest signupRequest) {
        return ResponseEntity.ok().body(authenticationService.signup(signupRequest));
    }

    @PostMapping("/signin")
    public ResponseEntity<AuthenticationResponse> signin(@RequestBody SigninRequest signinRequest) {
//        var headers = new HttpHeaders();
//        headers.add("Access-Control-Allow-Origin", "http://localhost:3000");
//        headers.add("Access-Control-Allow-Credentials", "true");
//        headers.add("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
//        headers.add("Access-Control-Allow-Headers", "Origin, Content-Type, Accept");

        System.out.println(signinRequest);

        return ResponseEntity
                .ok()
//                .headers(headers)
                .body(authenticationService.signin(signinRequest));
    }
}
