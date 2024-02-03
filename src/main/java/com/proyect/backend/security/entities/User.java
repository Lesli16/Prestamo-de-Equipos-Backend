package com.proyect.backend.security.entities;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "users")
@Getter @Setter
public class User {
    
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    @NotNull
    @Column(unique = true)
    private String userName;

    private String name;

    private String lastNames;

    @NotNull
    @Column(unique = true)
    private String email;

    private String phoneNumber;

    private String ci;

    @NotNull
    private String password;

    @ManyToOne(fetch = FetchType.EAGER)
    private Role role;
    public User() {
    }
    public User(@NotNull String userName, @NotNull String email, @NotNull String password) {
        this.userName = userName;
        this.email = email;
        this.password = password;
    }
}
