package com.maxkors.tweeder.domain;

import com.maxkors.tweeder.infrastructure.ProfileCardDTO;
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
    private final StorageService storageService;

    @Transactional
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Transactional
    public List<User> getSubscriptions(String username) {
        return userRepository.getSubscriptions(username);
    }

    @Transactional
    public List<User> getSubscribers(String username) {
        return userRepository.getSubscribers(username);
    }

    @Transactional
    public ProfileDTO getProfile(String username, org.springframework.security.core.userdetails.User principal) {
        return userRepository.getProfile(username, principal.getUsername());
    }

    @Transactional
    public void addFollower(String followerUsername, String subjectUsername) {
        this.userRepository.findByUsername(subjectUsername)
                .ifPresent(subject -> this.userRepository.findByUsername(followerUsername)
                        .ifPresent(follower -> subject.getSubscribers().add(follower)));
    }

    @Transactional
    public void removeFollower(String followerUsername, String subjectUsername) {
        this.userRepository.findByUsername(subjectUsername)
                .ifPresent(subject -> this.userRepository.findByUsername(followerUsername)
                        .ifPresent(follower -> subject.getSubscribers().remove(follower)));
    }

    @Transactional
    public List<ProfileCardDTO> getMatchingProfiles(String criteria) {
        return this.userRepository.getAllMatchingProfiles(criteria);
    }

    @Transactional
    public Optional<User> getUserData(String principalUsername) {
        return this.userRepository.getUserDataByUsername(principalUsername);
    }

    @Transactional
    public Optional<User> editUserData(String principalUsername, NewUserDataDTO userData) {
        return this.userRepository.getUserDataByUsername(principalUsername).map(user -> {
            if (!userData.avatar().isEmpty()) {
                String avatarFileName = this.storageService.uploadFile(userData.avatar());
                user.setAvatarUrl(avatarFileName);
            }

            user.setUsername(userData.username());
            user.setName(userData.name());
            user.setEmail(userData.email());

            return user;
        });
    }
}
