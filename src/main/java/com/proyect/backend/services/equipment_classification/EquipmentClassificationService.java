package com.proyect.backend.services.equipment_classification;

import com.proyect.backend.entities.EquipmentClassification;
import com.proyect.backend.repositories.EquipmentClassificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class EquipmentClassificationService {

    private final EquipmentClassificationRepository equipmentClassificationRepository;

    public void createEquipmentClassification(EquipmentClassification equipmentClassification){
        equipmentClassificationRepository.save(equipmentClassification);
    }

    public void updateEquipmentClassification(EquipmentClassification equipmentClassification){
        equipmentClassificationRepository.save(equipmentClassification);
    }

    public List<EquipmentClassification> findAll(){
        return equipmentClassificationRepository.findAll();
    }
}
