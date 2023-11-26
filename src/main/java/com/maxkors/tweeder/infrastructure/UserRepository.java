package com.maxkors.tweeder.infrastructure;

import com.maxkors.tweeder.security.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query("select distinct u from User u join fetch u.roles where u.username = :username")
    Optional<User> findByUsername(@Param("username") String username);
}
