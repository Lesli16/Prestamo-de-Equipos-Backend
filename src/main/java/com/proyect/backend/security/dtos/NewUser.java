package com.proyect.backend.security.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewUser {

    @NotEmpty
    private String userName;

    @NotEmpty
    private String name;

    @NotEmpty
    private String lastNames;

    @NotEmpty
    private String email;

    @NotEmpty
    private String phoneNumber;

    @NotEmpty
    private String ci;

    @NotEmpty
    private String password;

    @NotEmpty
    private String role;
}
