package com.proyect.backend.repositories;

import com.proyect.backend.entities.LoanRequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanRequestStatusRepository extends JpaRepository<LoanRequestStatus,Integer> {
}
