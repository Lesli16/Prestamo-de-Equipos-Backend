package com.proyect.backend.security.respositories;

import com.proyect.backend.security.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByUserName(String userName);
    boolean existsByUserName(String userName);
    Optional<User> findByEmail(String email);
}
