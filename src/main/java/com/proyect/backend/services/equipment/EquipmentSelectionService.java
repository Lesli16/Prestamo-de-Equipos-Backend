package com.proyect.backend.services.equipment;

import com.proyect.backend.entities.EquipmentSelection;
import com.proyect.backend.exceptions.GeneralException;
import com.proyect.backend.repositories.EquipmentSelectionRepository;
import com.proyect.backend.security.entities.User;
import com.proyect.backend.security.respositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class EquipmentSelectionService {

    private final EquipmentSelectionRepository equipmentSelectionRepository;
    private final UserRepository userRepository;

    public void createEquipmentSelection(EquipmentSelection equipmentSelection) throws GeneralException {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userName = userDetails.getUsername();
        Optional<User> user = userRepository.findByUserName(userName);

        Optional<EquipmentSelection> equipmentSelectionOpt =
                equipmentSelectionRepository.findByEquipment_IdAndUser_Id(equipmentSelection.getEquipment().getId(), user.get().getId());

        if (equipmentSelectionOpt.isPresent())
            throw new GeneralException("Este equipo ya ha sido seleccionado anteriormente para tu solicitud de préstamo");

        equipmentSelection.setUser(user.get());
        equipmentSelectionRepository.save(equipmentSelection);
    }

    public void updateEquipmentSelection(EquipmentSelection equipmentSelection) {
        equipmentSelectionRepository.save(equipmentSelection);
    }

    public List<EquipmentSelection> findAllByUserName() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userName = userDetails.getUsername();
        return equipmentSelectionRepository.findAllByUser_UserName(userName);
    }

    public void deleteEquipmentSelection(String id) {
        equipmentSelectionRepository.deleteById(id);
    }

    public void deleteAllEquipmentSelection(List<String> ids) {
        equipmentSelectionRepository.deleteAllById(ids);
    }
}
