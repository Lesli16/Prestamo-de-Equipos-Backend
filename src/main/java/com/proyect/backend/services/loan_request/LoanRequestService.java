package com.proyect.backend.services.loan_request;

import com.proyect.backend.entities.*;
import com.proyect.backend.exceptions.GeneralException;
import com.proyect.backend.repositories.EquipmentRepository;
import com.proyect.backend.repositories.EquipmentSelectionRepository;
import com.proyect.backend.repositories.LoanRequestRepository;
import com.proyect.backend.repositories.LoanSelectedEquipmentRepository;
import com.proyect.backend.security.entities.User;
import com.proyect.backend.security.enums.LoanRequestStatuses;
import com.proyect.backend.security.respositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class LoanRequestService {

    private final LoanRequestRepository loanRequestRepository;
    private final LoanSelectedEquipmentRepository loanSelectedEquipmentRepository;
    private final EquipmentSelectionRepository equipmentSelectionRepository;
    private final UserRepository userRepository;
    private final EquipmentRepository equipmentRepository;

    public void createLoanRequest(LoanRequest loanRequest) throws GeneralException {
        checkIfOutOfStock(loanRequest.getSelectedEquipments());

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userName = userDetails.getUsername();
        Optional<User> user = userRepository.findByUserName(userName);

        LocalDate actualDate = LocalDate.now();
        loanRequest.setDateCreated(actualDate);
        loanRequest.setUser(user.get());

        LoanRequestStatus loanRequestStatus = new LoanRequestStatus(1, LoanRequestStatuses.PENDIENTE);
        loanRequest.setLoanRequestStatus(loanRequestStatus);

        //Crea la solicitud de préstamo de los equipos seleccionados
        LoanRequest loanRequestSaved = loanRequestRepository.save(loanRequest);

        //Guardar listado de equipos que el usuario solicito
        createLoanSelectedEquipment(loanRequest.getSelectedEquipments(), loanRequestSaved);

        //Actualiza la cantidad de equipos disponibles y préstamo
        updateAvailableAndOnLoanEquipments(loanRequest.getSelectedEquipments());
        //Eliminar los equipos de la lista de solicitud
        deleteAllEquipmentSelection(userName);
    }

    private void createLoanSelectedEquipment(List<LoanSelectedEquipment> selectedEquipments, LoanRequest loanRequest) {
        for (LoanSelectedEquipment loanSelectedEquipment : selectedEquipments) {
            loanSelectedEquipment.setLoanRequest(loanRequest);
        }
        loanSelectedEquipmentRepository.saveAll(selectedEquipments);
    }

    public void deleteAllEquipmentSelection(String userName) {
        List<String> ids = equipmentSelectionRepository.findAllByUser_UserName(userName)
                .stream().map(EquipmentSelection::getId).collect(Collectors.toList());
        equipmentSelectionRepository.deleteAllById(ids);
    }

    public void updateAvailableAndOnLoanEquipments(List<LoanSelectedEquipment> selectedEquipments) {
        List<Equipment> equipmentList = new ArrayList<>();

        for (LoanSelectedEquipment selectedEquipment : selectedEquipments) {

            Integer prevTotalLoan = selectedEquipment.getEquipment().getOnLoanQuantity();
            Integer newTotalLoan = prevTotalLoan + selectedEquipment.getQuantity();
            selectedEquipment.getEquipment().setOnLoanQuantity(newTotalLoan);

            Integer prevTotalAvailable = selectedEquipment.getEquipment().getAvailableQuantity();
            Integer newTotalAvailable = prevTotalAvailable - selectedEquipment.getQuantity();

            selectedEquipment.getEquipment().setAvailableQuantity(newTotalAvailable);
            equipmentList.add(selectedEquipment.getEquipment());
        }
        equipmentRepository.saveAll(equipmentList);
    }

    public void checkIfOutOfStock(List<LoanSelectedEquipment> selectedEquipments) throws GeneralException {
        for (LoanSelectedEquipment selectedEquipment : selectedEquipments) {
            Integer requestedQuantity = selectedEquipment.getQuantity();
            int availableQuantity = selectedEquipment.getEquipment().getAvailableQuantity();

            boolean outOfStock = requestedQuantity > availableQuantity;
            boolean quantityLessThanZero = requestedQuantity < 0;

            if (outOfStock)
                throw new GeneralException("La cantidad del equipo '" + selectedEquipment.getEquipment().getName() + "' solicitado (" + requestedQuantity + ") supera la cantidad disponible (" + availableQuantity + ")");
            if (quantityLessThanZero)
                throw new GeneralException("La cantidad del equipo '" + selectedEquipment.getEquipment().getName() + "' no puede ser menor a cero");
        }
    }

    public List<LoanRequest> findAll() {
        return loanRequestRepository.findAll();
    }

    public List<LoanRequest> findAllByUserName() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userName = userDetails.getUsername();
        return loanRequestRepository.findAllByUser_UserNameOrderByDateCreatedDesc(userName);
    }

    public List<LoanRequest> findAllByPendingStatus() {
        return loanRequestRepository.findAllByLoanRequestStatus_StatusOrderByDateCreatedDesc(LoanRequestStatuses.PENDIENTE);
    }

    public void approveOrDenyLoanRequest(String loanRequestId, String status) throws GeneralException {
        LoanRequest loanRequest = loanRequestRepository.findById(loanRequestId)
                .orElseThrow(() -> new GeneralException("No se encontró la solicitud: " + loanRequestId));
        if (status.equals(LoanRequestStatuses.APROBADO.name())) {
            LoanRequestStatus approved = new LoanRequestStatus(2, LoanRequestStatuses.APROBADO);
            loanRequest.setLoanRequestStatus(approved);
        }
        if (status.equals(LoanRequestStatuses.RECHAZADO.name())) {
            LoanRequestStatus denied = new LoanRequestStatus(3, LoanRequestStatuses.RECHAZADO);
            loanRequest.setLoanRequestStatus(denied);

            handleAvailableEquipment(loanRequest.getSelectedEquipments());
        }
        loanRequestRepository.save(loanRequest);
    }

    public void handleAvailableEquipment(List<LoanSelectedEquipment> selectedEquipments) {
        for (LoanSelectedEquipment selectedEquipment : selectedEquipments) {
            Equipment equipmentToUpdate = selectedEquipment.getEquipment();

            int newTotalOnLoanQuantity = equipmentToUpdate.getOnLoanQuantity() - selectedEquipment.getQuantity();
            equipmentToUpdate.setOnLoanQuantity(newTotalOnLoanQuantity);

            int newTotalAvailable= equipmentToUpdate.getAvailableQuantity() + selectedEquipment.getQuantity();
            equipmentToUpdate.setAvailableQuantity(newTotalAvailable);
        }
    }
}
