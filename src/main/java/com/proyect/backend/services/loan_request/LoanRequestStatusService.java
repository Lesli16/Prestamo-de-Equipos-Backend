package com.proyect.backend.services.loan_request;

import com.proyect.backend.entities.LoanRequestStatus;
import com.proyect.backend.repositories.LoanRequestStatusRepository;
import com.proyect.backend.security.enums.LoanRequestStatuses;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class LoanRequestStatusService {

    private final LoanRequestStatusRepository loanRequestStatusRepository;

    public void createLoanRequestStatuses() {
        LoanRequestStatus pendent = new LoanRequestStatus(LoanRequestStatuses.PENDIENTE);
        LoanRequestStatus approved = new LoanRequestStatus(LoanRequestStatuses.APROBADO);
        LoanRequestStatus rejected = new LoanRequestStatus(LoanRequestStatuses.RECHAZADO);

        loanRequestStatusRepository.save(pendent);
        loanRequestStatusRepository.save(approved);
        loanRequestStatusRepository.save(rejected);
    }
}
