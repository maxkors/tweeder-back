package com.maxkors.tweeder.infrastructure;

import com.maxkors.tweeder.security.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query("select distinct u from User u join fetch u.roles where u.username = :username")
    Optional<User> getByUsername(@Param("username") String username);

    @Query("select s from User u join u.subscriptions s where u.username = :username")
    List<User> getSubscriptions(@Param("username") String username);

    @Query("select s from User u join u.subscribers s where u.username = :username")
    List<User> getSubscribers(@Param("username") String username);

    //    @Query("select new com.maxkors.tweeder.infrastructure.Profile(u.username, u.name, COUNT(u.subscriptions), COUNT(u.subscribers)) from User u join u.subscribers join u.subscriptions where u.username = :username")
    @Query(value = """
                select u.id,
                       u.username,
                       u.name,
                       count(case u.id when s.subject_id then 1 end) as subscribersCount,
                       count(case u.id when s.follower_id then 1 end)  as subscriptionsCount,
                       bool_or((select au.id from app_user au where au.username = :principal) = s.follower_id) as isFollowed
                from app_user u
                         left join subscription s on u.id in (s.subject_id, s.follower_id)
                where u.username = :username
                group by u.id
            """, nativeQuery = true)
    ProfileDTO getProfile(@Param("username") String username, @Param("principal") String principal);

    Optional<User> findByUsername(String username);
}
