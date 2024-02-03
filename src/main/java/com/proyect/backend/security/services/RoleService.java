package com.proyect.backend.security.services;

import java.util.Optional;

import javax.transaction.Transactional;

import com.proyect.backend.security.entities.Role;
import com.proyect.backend.security.enums.Roles;
import com.proyect.backend.security.respositories.RoleRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    public Optional<Role> getByRoleName(Roles roleName){
        return roleRepository.findByRoleName(roleName);
    }
    public void createRole() {
        Role student = new Role(Roles.ROLE_STUDENT);
        Role teacher = new Role(Roles.ROLE_TEACHER);
        Role admin = new Role(Roles.ROLE_ADMIN);
        roleRepository.save(student);
        roleRepository.save(teacher);
        roleRepository.save(admin);
    }
}
