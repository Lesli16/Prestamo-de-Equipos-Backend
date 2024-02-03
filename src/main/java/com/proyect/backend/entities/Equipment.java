package com.proyect.backend.entities;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Setter
public class Equipment {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;

    @Lob
    private byte[] image;

    @NotEmpty(message = "El nombre es obligatorio")
    private String name;

    private String model;

    private String description;

    @NotNull(message = "El stock es obligatorio")
    @Min(value = 0, message = "El stock no puede estar vacío")
    private Integer stock;

    @NotNull(message = "La clasificación es obligatoria")
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    private EquipmentClassification equipmentClassification;

    private Integer availableQuantity;

    private Integer inMaintenanceQuantity;

    private Integer onLoanQuantity;
}
