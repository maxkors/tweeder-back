package com.maxkors.tweeder.infrastructure;

import com.maxkors.tweeder.security.Role;
import com.maxkors.tweeder.security.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(RoleName name);
}
