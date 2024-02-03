package com.proyect.backend.controllers;

import com.proyect.backend.entities.EquipmentClassification;
import com.proyect.backend.entities.ResponseMessage;
import com.proyect.backend.services.equipment_classification.EquipmentClassificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/equipment-classification")
@RequiredArgsConstructor
public class EquipmentClassificationController {

    private final EquipmentClassificationService equipmentClassificationService;
    @PostMapping
    public ResponseEntity<ResponseMessage> createEquipmentClassification(@RequestBody @Valid EquipmentClassification equipmentClassification) {
        equipmentClassificationService.createEquipmentClassification(equipmentClassification);
        return new ResponseEntity<>(new ResponseMessage("Clasificación creada"), HttpStatus.CREATED);
    }
    @PutMapping
    public ResponseEntity<ResponseMessage> updateEquipmentClassification(@RequestBody @Valid EquipmentClassification equipmentClassification) {
        equipmentClassificationService.updateEquipmentClassification(equipmentClassification);
        return new ResponseEntity<>(new ResponseMessage("Información actualizada"), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<EquipmentClassification>> findAllEquipmentClassification() {
        return ResponseEntity.status(HttpStatus.OK).body(equipmentClassificationService.findAll());
    }
}
