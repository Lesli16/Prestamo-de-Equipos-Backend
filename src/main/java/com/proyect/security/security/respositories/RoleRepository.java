package com.proyect.security.security.respositories;

import java.util.Optional;

import com.proyect.security.security.entities.Role;
import com.proyect.security.security.enums.Roles;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository <Role, Integer> {
    Optional<Role> findByRoleName(Roles roleName);
    
}
