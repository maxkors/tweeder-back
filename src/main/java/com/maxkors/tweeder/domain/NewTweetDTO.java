package com.maxkors.tweeder.domain;

import jakarta.annotation.Nullable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public record NewTweetDTO(String text, @Nullable Long parentPostId, List<MultipartFile> files) {
}
