package com.proyect.backend.repositories;

import com.proyect.backend.entities.LoanRequest;
import com.proyect.backend.security.enums.LoanRequestStatuses;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LoanRequestRepository extends JpaRepository<LoanRequest, String> {
    List<LoanRequest> findAllByUser_UserNameOrderByDateCreatedDesc(String userName);

    List<LoanRequest> findAllByLoanRequestStatus_StatusOrderByDateCreatedDesc(LoanRequestStatuses status);
}
