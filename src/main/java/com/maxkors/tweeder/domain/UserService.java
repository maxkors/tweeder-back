package com.maxkors.tweeder.domain;

import com.maxkors.tweeder.infrastructure.ProfileDTO;
import com.maxkors.tweeder.infrastructure.UserRepository;
import com.maxkors.tweeder.security.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public Optional<User> getByUsername(String username) {
        return userRepository.getByUsername(username);
    }

    @Transactional
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Transactional
    public List<User> getSubscriptions(String username) {
        return userRepository.getSubscriptions(username);
    }

    @Transactional
    public ProfileDTO getProfile(String username) {
        return userRepository.getProfile(username);
    }
}
