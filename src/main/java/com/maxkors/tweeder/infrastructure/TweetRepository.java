package com.maxkors.tweeder.infrastructure;

import com.maxkors.tweeder.domain.Tweet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TweetRepository extends JpaRepository<Tweet, Long> {
    @Query(value = """
             select t.id, u.name, u.username, t.text, t.likes, t.date_time, count(c) as commentsCount
             from Tweet t
             join app_user u on t.app_user_id = u.id
             left join comment c on t.id = c.tweet_id
             group by t.id, u.id
             """, nativeQuery = true)
    List<TweetPlainDTO> getAll();

    @Query("select distinct t from Tweet t left join fetch t.user left join fetch t.comments where t.id = :id")
    Optional<Tweet> getByIdEntirely(@Param("id") Long id);

    @Query(value = """
            select t.id, u.name, u.username, t.text, t.likes, t.date_time, count(c) as commentsCount
            from tweet t
            left join app_user u on t.app_user_id = u.id
            left join comment c on t.id = c.tweet_id
            where u.username = :username
            group by t.id, u.id
            """, nativeQuery = true)
    List<TweetPlainDTO> getByUsername(@Param("username") String username);

    @Query(value = """
                select t.id, u.name, u.username, t.text, t.likes, t.date_time, count(c) as commentsCount
                                        from app_user u
                                                 left join subscription s on u.id = s.follower_id
                                                 left join app_user su on s.subject_id = su.id
                                                 left join tweet t on su.id = t.app_user_id
                                                 left join comment c on t.id = c.tweet_id
                                        where u.username = 'maximus'
                                        group by t.id, t.date_time, u.id
                                        order by t.date_time desc
            """, nativeQuery = true)
    List<TweetPlainDTO> getFromUserSubscriptions(@Param("username") String username);
}
