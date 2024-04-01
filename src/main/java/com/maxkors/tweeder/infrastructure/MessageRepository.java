package com.maxkors.tweeder.infrastructure;

import com.maxkors.tweeder.domain.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {
}
