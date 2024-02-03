package com.proyect.backend.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class LoanSelectedEquipment {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    @ManyToOne(fetch = FetchType.EAGER)
    private Equipment equipment;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonBackReference
    private LoanRequest loanRequest;

    private Integer quantity;
}
