package com.proyect.backend.services.equipment;

import com.proyect.backend.dtos.CreateEquipmentDto;
import com.proyect.backend.entities.Equipment;
import com.proyect.backend.entities.ResponseMessage;
import com.proyect.backend.exceptions.GeneralException;
import com.proyect.backend.repositories.EquipmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class EquipmentService {

    private final EquipmentRepository equipmentRepository;

    public void createEquipment(Equipment equipment, MultipartFile image) throws IOException {
        if (image != null) equipment.setImage(image.getBytes());
        equipment.setAvailableQuantity(equipment.getStock());
        equipment.setInMaintenanceQuantity(0);
        equipment.setOnLoanQuantity(0);
        equipmentRepository.save(equipment);
    }

    public void updateEquipment(Equipment equipment, MultipartFile image) throws IOException, GeneralException {
        if (image == null) {
            equipment.setImage(null);
        } else {
            equipment.setImage(image.getBytes());
        }


        int total = equipment.getInMaintenanceQuantity() + equipment.getOnLoanQuantity();

        int totalAvailable = equipment.getStock() - equipment.getOnLoanQuantity() - equipment.getInMaintenanceQuantity();
        equipment.setAvailableQuantity(totalAvailable);
        if (total > equipment.getStock()) {
            throw new GeneralException("La suma de equipos en mantenimiento y préstamo supera el stock disponible.");
        }


        equipmentRepository.save(equipment);
    }

    public List<Equipment> findAll() {
        return equipmentRepository.findAll();
    }

}
