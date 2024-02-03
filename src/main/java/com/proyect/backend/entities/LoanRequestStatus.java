package com.proyect.backend.entities;

import com.proyect.backend.security.enums.LoanRequestStatuses;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
@Entity
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class LoanRequestStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotNull
    @Enumerated(EnumType.STRING)
    private LoanRequestStatuses status;

    public LoanRequestStatus(LoanRequestStatuses status) {
        this.status = status;
    }
}
