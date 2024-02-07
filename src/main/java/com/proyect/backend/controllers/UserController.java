package com.proyect.backend.controllers;

import com.proyect.backend.entities.ResponseMessage;
import com.proyect.backend.exceptions.GeneralException;
import com.proyect.backend.security.entities.User;
import com.proyect.backend.security.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @PutMapping
    public ResponseEntity<ResponseMessage> updateUserInfo(@RequestBody User user){
        userService.updateUserInfo(user);
        return new ResponseEntity<>(new ResponseMessage("Información actualizada"), HttpStatus.OK);
    }

    @PutMapping("/change-password")
    public ResponseEntity<ResponseMessage> updateUserInfo(@RequestParam String newPassword) throws GeneralException {
        userService.changePassword(newPassword);
        return new ResponseEntity<>(new ResponseMessage("Contraseña actualizada"), HttpStatus.OK);
    }
}
