package com.proyect.backend.entities;

import com.proyect.backend.security.entities.User;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class EquipmentSelection {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    @ManyToOne(fetch = FetchType.EAGER)
    private Equipment equipment;

    private Integer quantity;

    @ManyToOne(fetch = FetchType.EAGER)
    private User user;
}
