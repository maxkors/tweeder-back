package com.maxkors.tweeder.domain;

import org.springframework.web.multipart.MultipartFile;

public record NewUserDataDTO(String username, String name, String email, MultipartFile avatar) {
}
