package com.proyect.backend.repositories;

import com.proyect.backend.entities.LoanRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanRequestRepository extends JpaRepository<LoanRequest, String> {
}
