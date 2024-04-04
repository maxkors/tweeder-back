package com.maxkors.tweeder.infrastructure;

import com.maxkors.tweeder.domain.Chat;
import com.maxkors.tweeder.domain.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface ChatRepository extends JpaRepository<Chat, Long> {

    @Query("""
            select c
            from User u
            left join u.chats c
            join fetch c.participants
            where u.username = :username
            """)
    List<Chat> getAllByUsername(@Param("username") String username);

    @Query("""
            select m
            from Message m
            where m.chat.id = :id
            """)
    List<Message> getAllMessagesByChatId(@Param("id") Long id);
}
