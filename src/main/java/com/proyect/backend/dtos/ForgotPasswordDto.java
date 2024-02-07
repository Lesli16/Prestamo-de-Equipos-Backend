package com.proyect.backend.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
@Getter @Setter
@AllArgsConstructor
public class ForgotPasswordDto {

    @NotEmpty(message = "La nueva contraseña es obligatoria")
    private String newPassword;
    @NotEmpty(message = "El token es obligatorio")
    private String resetPasswordToken;
}
