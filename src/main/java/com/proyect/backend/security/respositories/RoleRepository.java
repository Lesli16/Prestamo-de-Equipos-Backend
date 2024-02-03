package com.proyect.backend.security.respositories;

import java.util.Optional;

import com.proyect.backend.security.entities.Role;
import com.proyect.backend.security.enums.Roles;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository <Role, Integer> {
    Optional<Role> findByRoleName(Roles roleName);
    
}
