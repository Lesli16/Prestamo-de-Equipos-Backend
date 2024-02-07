package com.proyect.backend.security.controllers;

import com.proyect.backend.dtos.ForgotPasswordDto;
import com.proyect.backend.entities.ResponseMessage;
import com.proyect.backend.exceptions.GeneralException;
import com.proyect.backend.security.dtos.LoginUser;
import com.proyect.backend.security.dtos.NewUser;
import com.proyect.backend.security.entities.User;
import com.proyect.backend.security.services.AuthService;
import com.proyect.backend.security.services.UserService;
import com.proyect.backend.security.util.CookieUtil;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
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
    private final UserService userService;
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

    @GetMapping("/user-details")
    public ResponseEntity<User> getUserDetails(){
        return ResponseEntity.status(HttpStatus.OK).body(userService.getUserDetails());
    }

    @GetMapping("/logout")
    public ResponseEntity<ResponseMessage> logout(HttpServletResponse httpServletResponse) {
        try {
            CookieUtil.clear(httpServletResponse, COOKIE_NAME);
            return new ResponseEntity<>(new ResponseMessage("Se ha cerrado la sesión"), HttpStatus.OK);
        } catch (Exception e) {
            CookieUtil.clear(httpServletResponse, COOKIE_NAME);
            return new ResponseEntity<>(new ResponseMessage("Se ha cerrado la sesión desde la excepción"), HttpStatus.OK);
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ResponseMessage> sendForgotPasswordEmail(@RequestParam String email) throws GeneralException, MessagingException {
        userService.sendForgotPasswordEmail(email);
        String responseMessage = "Pronto, recibirás un e-mail para restablecer tu contraseña. Si no puedes encontrarlo revisa tu spam";
        return new ResponseEntity<>(new ResponseMessage(responseMessage), HttpStatus.OK);
    }

    @PutMapping("/reset-password")
    public ResponseEntity<ResponseMessage> resetPassword(@RequestBody @Valid ForgotPasswordDto forgotPasswordDto) throws GeneralException {
        userService.resetPassword(forgotPasswordDto);
        return new ResponseEntity<>(new ResponseMessage("Contraseña actualizada"), HttpStatus.OK);
    }

    @GetMapping("/validate-token")
    public boolean validateResetPasswordToken(@RequestParam String resetTokenPassword) {
        return userService.validateResetTokenPassword(resetTokenPassword);
    }
}
