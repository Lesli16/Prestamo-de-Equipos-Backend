package com.proyect.backend.dtos;

import com.proyect.backend.entities.EquipmentClassification;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter @Setter
public class CreateEquipmentDto {

    @NotEmpty(message = "El nombre es obligatorio")
    private String name;

    private String model;

    private String description;

    @NotNull
    @Min(value = 1, message = "El stock debe ser al menos 1")
    private Integer stock;

    private EquipmentClassification equipmentClassification;

    private Integer availableQuantity;

    private Integer inMaintenanceQuantity;

    private Integer onLoanQuantity;
}
