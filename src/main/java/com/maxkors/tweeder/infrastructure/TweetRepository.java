package com.maxkors.tweeder.infrastructure;

import com.maxkors.tweeder.domain.Tweet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface TweetRepository extends JpaRepository<Tweet, Long> {
    @Query("""
             select t
             from Tweet t join t.user""")
    List<Tweet> getAll();

    @Query("""
            select distinct t
            from Tweet t
                left join fetch t.user
                left join fetch t.children
            where t.id = :id""")
    Optional<Tweet> getByIdEntirely(@Param("id") Long id);

    @Query("""
            select distinct t
            from Tweet t
                left join t.likes
            where t.id = :id""")
    Optional<Tweet> getByIdWithLikes(@Param("id") Long id);

    @Query("""
            select distinct t
            from Tweet t
                left join t.bookmarkers
            where t.id = :id""")
    Optional<Tweet> getByIdWithBookmarkers(@Param("id") Long id);

    @Query("""
            select distinct t
            from Tweet t
                left join t.bookmarkers
            where t.user.username = :username""")
    List<Tweet> getAllBookmarkedByUser(@Param("username") String username);

    @Query("""
            select t
            from Tweet t
                join t.user
            where t.user.username = :username""")
    List<Tweet> getByUsernameAll(@Param("username") String username);

    @Query("""
            select t
            from Tweet t
                join t.user
            where t.user.username = :username and t.parent = null""")
    List<Tweet> getByUsernameParents(@Param("username") String username);

    @Query("""
            select t
            from User u
                join u.likedTweets t
            where u.username = :username""")
    List<Tweet> getLikedByUsername(@Param("username") String username);

//    @Query("""
//                select t
//                from User u
//                    left join u.subscriptions s
//                    left join s.tweets t
//                where u.username = :username and t.parent = null
//
//                union
//
//                select t
//                from User u
//                    left join u.tweets t
//                where u.username = :username and t.parent = null
//
//            """)
////    order by t.dateTime desc
//    List<Tweet> getFromUserSubscriptions(@Param("username") String username);

    @Query("""
                select t
                from User u
                    left join u.subscriptions s
                    left join s.tweets t
                where u.username = :username and t.parent = null              
            """)
//    order by t.dateTime desc
    List<Tweet> getFromUserSubscriptions(@Param("username") String username);

    @Query("""
                select t.id
                from Tweet t
                left join t.likes l
                where l.username = :username and t.id in :ids
            """)
    Set<Long> getLikedPostIdsFromList(String username, List<Long> ids);

    @Query("""
                select t.id
                from Tweet t
                left join t.bookmarkers b
                where b.username = :username and t.id in :ids
            """)
    Set<Long> getBookmarkedPostIdsFromList(String username, List<Long> ids);
}
