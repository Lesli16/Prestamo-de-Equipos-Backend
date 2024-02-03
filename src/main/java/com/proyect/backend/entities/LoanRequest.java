package com.proyect.backend.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.proyect.backend.security.entities.User;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
public class LoanRequest {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    private LocalDate dateCreated;

    @ManyToOne(fetch = FetchType.EAGER)
    private User user;

    @OneToMany(mappedBy = "loanRequest", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<LoanSelectedEquipment> selectedEquipments;

    @ManyToOne(fetch = FetchType.EAGER)
    private LoanRequestStatus loanRequestStatus;

    private LocalDate loanDuration;
}
