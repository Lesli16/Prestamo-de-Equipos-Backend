package com.proyect.security.security.controllers;

import com.proyect.security.entities.ResponseMessage;
import com.proyect.security.security.dtos.LoginUser;
import com.proyect.security.security.dtos.NewUser;
import com.proyect.security.security.services.AuthService;
import com.proyect.security.security.util.CookieUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    @Value(value = "${cookie.domain}")
    private String cookieDomain;

    private final AuthService authService;
    private static final String COOKIE_NAME = "AccessToken";

    @PostMapping("/login")
    public ResponseEntity<Object> login(@Valid @RequestBody LoginUser loginUser, HttpServletResponse response, BindingResult bidBindingResult) {
        if (bidBindingResult.hasErrors())
            return new ResponseEntity<>(new ResponseMessage("Usuario o contraseña incorrectos, intente de nuevo"), HttpStatus.BAD_REQUEST);
        try {
            List<String> signInResults = authService.handleLogin(loginUser);
            String accessToken = signInResults.get(0);
            String userRole = signInResults.get(1);
            CookieUtil.create(response, COOKIE_NAME, accessToken, true, -1, cookieDomain);
            return new ResponseEntity<>(new ResponseMessage(userRole), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseMessage("Usuario o contraseña incorrectos, intente de nuevo"), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<Object> register(@Valid @RequestBody NewUser newUser, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return new ResponseEntity<>(new ResponseMessage("Revise los campos e intente nuevamente"), HttpStatus.BAD_REQUEST);
        try {
            authService.handleSignUp(newUser);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseMessage("El email ya se encuentra registrado"), HttpStatus.BAD_REQUEST);

        }
        return new ResponseEntity<>(new ResponseMessage("Inicie sesión para continuar"), HttpStatus.CREATED);
    }
}
