package com.maxkors.tweeder.security;

import com.maxkors.tweeder.infrastructure.RoleRepository;
import com.maxkors.tweeder.infrastructure.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public AuthenticationResponse signup(SignupRequest signupRequest) {
        Role userRole = roleRepository.findByName(RoleName.ROLE_USER);

        var user = User.builder()
                .username(signupRequest.username())
                .password(passwordEncoder.encode(signupRequest.password()))
                .name(signupRequest.name())
                .email(signupRequest.email())
                .roles(Set.of(userRole))
                .build();

        userRepository.save(user);

        String jwtToken = jwtService.generateToken(UserDetailsServiceImpl.mapUserToUserDetails(user));

        return new AuthenticationResponse(jwtToken);
    }

    @Transactional
    public AuthenticationResponse signin(SigninRequest signinRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(signinRequest.username(), signinRequest.password()));

        var user = userRepository.findByUsername(signinRequest.username()).orElseThrow();

        String jwtToken = jwtService.generateToken(UserDetailsServiceImpl.mapUserToUserDetails(user));

        return new AuthenticationResponse(jwtToken);
    }
}
