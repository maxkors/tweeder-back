package com.maxkors.tweeder.infrastructure;

import com.maxkors.tweeder.domain.Tweet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TweetRepository extends JpaRepository<Tweet, Long> {

    @Query("select distinct t from Tweet t join fetch t.user")
    List<Tweet> getAll();

    @Query("select distinct t from Tweet t join fetch t.user join fetch t.comments where t.id = :id")
    Tweet getById(@Param("id") Long id);
}
