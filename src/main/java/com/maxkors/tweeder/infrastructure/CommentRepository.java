package com.maxkors.tweeder.infrastructure;

import com.maxkors.tweeder.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("select c from Comment c where c.user.username = :username")
    List<Comment> getByAllByUsername(@Param("username") String username);
}
