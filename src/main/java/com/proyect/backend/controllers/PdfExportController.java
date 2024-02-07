package com.proyect.backend.controllers;

import com.proyect.backend.entities.LoanRequest;
import com.proyect.backend.services.pdf.LoanRequestPdfService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/pdf")
public class PdfExportController {

    private final LoanRequestPdfService loanRequestPdfService;

    @PostMapping("/loan-request")
    public void generateLoanRequestPdf(@RequestBody LoanRequest loanRequest, HttpServletResponse response) throws IOException {
        response.setContentType("application/pdf");
        String headerKey = "Content-Disposition";
        String headerValue = "inline; filename=pdf_" + loanRequest.getUser().getFullName() + ".pdf";
        response.setHeader(headerKey, headerValue);

        loanRequestPdfService.generateLoanRequestPdf(loanRequest, response);
    }
}
