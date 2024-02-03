package com.proyect.backend.repositories;

import com.proyect.backend.entities.EquipmentSelection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EquipmentSelectionRepository extends JpaRepository<EquipmentSelection, String> {

    Optional<EquipmentSelection> findByEquipment_IdAndUser_Id(String equipmentId, String userId);
    List<EquipmentSelection> findAllByUser_UserName(String userName);

}
