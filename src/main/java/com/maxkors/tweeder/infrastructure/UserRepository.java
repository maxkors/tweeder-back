package com.maxkors.tweeder.infrastructure;

import com.maxkors.tweeder.security.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query("select distinct u from User u join fetch u.roles where u.username = :username")
    User findByUsername(@Param("username") String username);
}
