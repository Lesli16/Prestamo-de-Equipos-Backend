package com.proyect.security.security.services;

import com.proyect.security.security.dtos.LoginUser;
import com.proyect.security.security.dtos.NewUser;
import com.proyect.security.security.entities.Role;
import com.proyect.security.security.entities.User;
import com.proyect.security.security.enums.Roles;
import com.proyect.security.security.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final RoleService roleService;
    private final JwtProvider jwtProvider;

    public List<String> handleLogin(LoginUser loginUser) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginUser.getUserName(), loginUser.getPassword());
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken = jwtProvider.generateToken(authentication);
        String userRole = jwtProvider.getRoleFromToken(accessToken);

        List<String> responseList = new ArrayList<>();
        responseList.add(accessToken);
        responseList.add(userRole);
        return responseList;
    }

    public void handleSignUp(NewUser newUser) {
        String passwordEncoded = passwordEncoder.encode(newUser.getPassword());

        User user = new User();
        user.setUserName(newUser.getUserName());
        user.setName(newUser.getName());
        user.setLastNames(newUser.getLastNames());
        user.setEmail(newUser.getEmail());
        user.setPhoneNumber(newUser.getPhoneNumber());
        user.setCi(newUser.getCi());
        user.setPassword(passwordEncoded);

        Role role;
        if (newUser.getRole().equals("student")) {
            role = roleService.getByRoleName(Roles.ROLE_STUDENT).get();
        } else if (newUser.getRole().equals("teacher")) {
            role = roleService.getByRoleName(Roles.ROLE_TEACHER).get();
        } else {
            role = roleService.getByRoleName(Roles.ROLE_ADMIN).get();
        }

        user.setRole(role);
        userService.save(user);
    }
}
