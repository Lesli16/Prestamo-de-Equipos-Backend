package com.proyect.backend.controllers;

import com.proyect.backend.entities.EquipmentSelection;
import com.proyect.backend.entities.ResponseMessage;
import com.proyect.backend.exceptions.GeneralException;
import com.proyect.backend.services.equipment.EquipmentSelectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/equipment-selection")
public class EquipmentSelectionController {

    private final EquipmentSelectionService equipmentSelectionService;

    @GetMapping("/by-username")
    public ResponseEntity<List<EquipmentSelection>> findAllEquipmentSelection() {
        return ResponseEntity.status(HttpStatus.OK).body(equipmentSelectionService.findAllByUserName());
    }

    @PostMapping
    public ResponseEntity<ResponseMessage> createEquipmentSelection(@RequestBody EquipmentSelection equipmentSelection) throws GeneralException {
        equipmentSelectionService.createEquipmentSelection(equipmentSelection);
        return new ResponseEntity<>(new ResponseMessage("El equipo se ha añadido a tu solicitud de préstamo"), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<ResponseMessage> updateEquipmentSelection(@RequestBody EquipmentSelection equipmentSelection) {
        equipmentSelectionService.updateEquipmentSelection(equipmentSelection);
        return new ResponseEntity<>(new ResponseMessage("El equipo se ha actualizado"), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseMessage> deleteEquipmentSelectionById(@PathVariable String id) {
        equipmentSelectionService.deleteEquipmentSelection(id);
        return new ResponseEntity<>(new ResponseMessage("El equipo se ha eliminado de tu solicitud de préstamo"), HttpStatus.CREATED);
    }

    @DeleteMapping
    public void deleteAllEquipmentSelection(@RequestParam List<String> ids) {
        equipmentSelectionService.deleteAllEquipmentSelection(ids);
    }
}
