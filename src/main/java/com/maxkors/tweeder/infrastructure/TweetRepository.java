package com.maxkors.tweeder.infrastructure;

import com.maxkors.tweeder.domain.Tweet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TweetRepository extends JpaRepository<Tweet, Long> {
    @Query("""
             select new com.maxkors.tweeder.infrastructure.TweetPlainDTO(t.id, t.user, t.text, t.likesCount, t.commentsCount, t.dateTime)
             from Tweet t join t.user""")
    List<TweetPlainDTO> getAll();

    @Query("""
            select distinct t
            from Tweet t
                left join fetch t.user
                left join fetch t.comments
            where t.id = :id""")
    Optional<Tweet> getByIdEntirely(@Param("id") Long id);

    @Query("""
            select new com.maxkors.tweeder.infrastructure.TweetPlainDTO(t.id, t.user, t.text, t.likesCount, t.commentsCount, t.dateTime)
            from Tweet t
                join t.user
            where t.user.username = :username""")
    List<TweetPlainDTO> getByUsername(@Param("username") String username);

    @Query("""
                select new com.maxkors.tweeder.infrastructure.TweetPlainDTO(t.id, t.user, t.text, t.likesCount, t.commentsCount, t.dateTime)
                from User u
                    left join u.subscriptions s
                    left join s.tweets t
                where u.username = :username
                
                union
                select new com.maxkors.tweeder.infrastructure.TweetPlainDTO(t.id, t.user, t.text, t.likesCount, t.commentsCount, t.dateTime)
                from User u
                    left join u.tweets t
                where u.username = :username
                
            """)
//    order by t.dateTime desc
    List<TweetPlainDTO> getFromUserSubscriptions(@Param("username") String username);
}
