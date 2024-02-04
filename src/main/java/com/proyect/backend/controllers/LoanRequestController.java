package com.proyect.backend.controllers;

import com.proyect.backend.entities.LoanRequest;
import com.proyect.backend.entities.ResponseMessage;
import com.proyect.backend.exceptions.GeneralException;
import com.proyect.backend.services.loan_request.LoanRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/loan-request")
@RequiredArgsConstructor
public class LoanRequestController {

    private final LoanRequestService loanRequestService;

    @PostMapping
    public void createLoanRequest(@RequestBody LoanRequest loanRequest) throws GeneralException {
        loanRequestService.createLoanRequest(loanRequest);
    }
    @GetMapping
    public ResponseEntity<List<LoanRequest>> findAllLoanRequest(){
        return ResponseEntity.status(HttpStatus.OK).body(loanRequestService.findAll());
    }

    @GetMapping("/all/by-username")
    public ResponseEntity<List<LoanRequest>> findAllLoanRequestByUserName(){
        return ResponseEntity.status(HttpStatus.OK).body(loanRequestService.findAllByUserName());
    }

    @GetMapping("/status/pending")
    public ResponseEntity<List<LoanRequest>> findAllPendingStatusLoanRequest(){
        return ResponseEntity.status(HttpStatus.OK).body(loanRequestService.findAllByPendingStatus());
    }

    @PutMapping("/approve-deny")
    public ResponseEntity<ResponseMessage> approveOrDenyLoanRequest(@RequestParam String loanRequestId,
                                                                    @RequestParam String status) throws GeneralException {
        loanRequestService.approveOrDenyLoanRequest(loanRequestId, status);
        return new ResponseEntity<>(new ResponseMessage("La solicitud ha sido " + status.toLowerCase()), HttpStatus.OK);
    }
}
