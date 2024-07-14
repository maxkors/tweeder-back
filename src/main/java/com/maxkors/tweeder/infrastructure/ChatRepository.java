package com.maxkors.tweeder.infrastructure;

import com.maxkors.tweeder.domain.Chat;
import com.maxkors.tweeder.domain.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


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

    @Query(value = """
            select auc.chat_id as id
            from app_user__chat auc
            group by auc.chat_id
            having bool_and(auc.app_user_id in (coalesce((select u.id from app_user u where u.username = :username1), -1),
                                                coalesce((select u.id from app_user u where u.username = :username2), -1)))
            """, nativeQuery = true)
    Optional<Chat> getChatByParticipantUsernames(@Param("username1") String username1, @Param("username2") String username2);
}
