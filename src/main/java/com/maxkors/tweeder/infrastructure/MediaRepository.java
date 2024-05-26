package com.maxkors.tweeder.infrastructure;

import com.maxkors.tweeder.domain.Media;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MediaRepository extends JpaRepository<Media, Long> {
}
