package com.maxkors.tweeder.infrastructure;

import com.maxkors.tweeder.security.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
