package com.proyect.backend.controllers;

import com.proyect.backend.dtos.CreateEquipmentDto;
import com.proyect.backend.entities.Equipment;
import com.proyect.backend.entities.EquipmentClassification;
import com.proyect.backend.entities.ResponseMessage;
import com.proyect.backend.exceptions.GeneralException;
import com.proyect.backend.services.equipment.EquipmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/equipment")
@RequiredArgsConstructor
public class EquipmentController {

    private final EquipmentService equipmentService;

    @PostMapping
    public ResponseEntity<ResponseMessage> createEquipment(@RequestPart("equipment") @Valid Equipment equipment,
                                                           @RequestPart(value = "image", required = false) MultipartFile image) throws IOException {
        equipmentService.createEquipment(equipment, image);
        return new ResponseEntity<>(new ResponseMessage("Equipo creado"), HttpStatus.CREATED);
    }
    @PutMapping
    public ResponseEntity<ResponseMessage> updateEquipment(@RequestPart("equipment") @Valid Equipment equipment,
                                                           @RequestPart(value = "image", required = false) MultipartFile image) throws IOException, GeneralException {
        equipmentService.updateEquipment(equipment, image);
        return new ResponseEntity<>(new ResponseMessage("Equipo actualizado"), HttpStatus.OK);
    }
    @GetMapping
    public ResponseEntity<List<Equipment>> findAllEquipments() {
        return ResponseEntity.status(HttpStatus.OK).body(equipmentService.findAll());
    }
}
