package com.proyect.backend.services.pdf;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.proyect.backend.entities.Equipment;
import com.proyect.backend.entities.LoanRequest;
import com.proyect.backend.entities.LoanSelectedEquipment;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Service
public class LoanRequestPdfService {
    private float pageWidth;

    public void generateLoanRequestPdf(LoanRequest loanRequest, HttpServletResponse response) throws IOException {
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();
        pageWidth = document.getPageSize().getWidth() - document.leftMargin() - document.rightMargin();

        Font fontTitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);

        Paragraph title = new Paragraph("Detalle solicitud de préstamos de equipos", fontTitle);
        title.setAlignment(Paragraph.ALIGN_CENTER);
        title.setSpacingAfter(10);

        Font fontBold = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
        Font fontNormal = FontFactory.getFont(FontFactory.HELVETICA, 12);

        Paragraph user = new Paragraph();
        user.add(new Chunk("Usuario: ", fontBold));
        user.add(new Chunk(loanRequest.getUser().getFullName(), fontNormal));
        user.setSpacingAfter(5);

        Paragraph role = new Paragraph();
        role.add(new Chunk("Cargo: ", fontBold));
        role.add(new Chunk(getUserRole(loanRequest.getUser().getRole().getRoleName().name()), fontNormal));
        role.setSpacingAfter(5);

        Paragraph email = new Paragraph();
        email.add(new Chunk("Email: ", fontBold));
        email.add(new Chunk(loanRequest.getUser().getEmail(), fontNormal));
        email.setSpacingAfter(5);

        Paragraph loanRequestCreated = new Paragraph();
        loanRequestCreated.add(new Chunk("Fecha de préstamo: ", fontBold));
        loanRequestCreated.add(new Chunk(String.valueOf(loanRequest.getDateCreated()), fontNormal));
        loanRequestCreated.setSpacingAfter(5);

        Paragraph loanRequestDuration = new Paragraph();
        loanRequestDuration.add(new Chunk("Duración de préstamo: ", fontBold));
        loanRequestDuration.add(new Chunk(String.valueOf(loanRequest.getLoanDuration()), fontNormal));
        loanRequestDuration.setSpacingAfter(5);

        document.add(title);
        document.add(user);
        document.add(role);
        document.add(email);
        document.add(loanRequestCreated);
        document.add(loanRequestDuration);
        document.add(equipmentsRequest(loanRequest.getSelectedEquipments()));
        document.add(equipmentsRequestSignature(loanRequest));
        document.close();
    }

    private String getUserRole(String role) {
        if (role.equals("ROLE_STUDENT")) {
            return "Estudiante";
        } else {
            return "Docente";
        }
    }

    private PdfPTable equipmentsRequest(List<LoanSelectedEquipment> loanSelectedEquipmentList) {
        PdfPTable equipmentsTable = new PdfPTable(5);
        float[] columnsWidth = {56, 56, 56, 56, 56};
        equipmentsTable.setWidths(columnsWidth);
        equipmentsTable.setSpacingBefore(15);
        equipmentsTable.setTotalWidth(pageWidth);
        equipmentsTable.setLockedWidth(true);

        Font fontNormal = FontFactory.getFont(FontFactory.HELVETICA, 12);
        Font fontBold = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);

        PdfPCell name = createStyledCell("Nombre", fontBold);
        PdfPCell model = createStyledCell("Modelo", fontBold);
        PdfPCell description = createStyledCell("Descripción", fontBold);
        PdfPCell quantity = createStyledCell("Cantidad", fontBold);
        PdfPCell classification = createStyledCell("Clasificación", fontBold);

        equipmentsTable.addCell(name);
        equipmentsTable.addCell(model);
        equipmentsTable.addCell(description);
        equipmentsTable.addCell(quantity);
        equipmentsTable.addCell(classification);

        for (LoanSelectedEquipment loanSelectedEquipment : loanSelectedEquipmentList) {
            Equipment equipment = loanSelectedEquipment.getEquipment();
            PdfPCell nameValue = createStyledCell(equipment.getName(), fontNormal);
            PdfPCell modelValue = createStyledCell(equipment.getModel(), fontNormal);
            PdfPCell descriptionValue = createStyledCell(equipment.getDescription(), fontNormal);
            PdfPCell quantityValue = createStyledCell(String.valueOf(loanSelectedEquipment.getQuantity()), fontNormal);
            PdfPCell classificationValue = createStyledCell(equipment.getEquipmentClassification().getName(), fontNormal);

            equipmentsTable.addCell(nameValue);
            equipmentsTable.addCell(modelValue);
            equipmentsTable.addCell(descriptionValue);
            equipmentsTable.addCell(quantityValue);
            equipmentsTable.addCell(classificationValue);
        }
        return equipmentsTable;
    }

    private PdfPTable equipmentsRequestSignature(LoanRequest loanRequest) {
        PdfPTable signatureTable = new PdfPTable(4);
        float[] columnsWidth = {70, 70, 70, 70};
        signatureTable.setWidths(columnsWidth);
        signatureTable.setSpacingBefore(50);
        signatureTable.setTotalWidth(pageWidth);
        signatureTable.setKeepTogether(true);
        signatureTable.setLockedWidth(true);

        Font fontBold = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);

        PdfPCell emptyCell = new PdfPCell(new Paragraph(""));
        emptyCell.setBorder(Rectangle.NO_BORDER);

        PdfPCell emptyCellWithBottomBorden = new PdfPCell(new Paragraph(""));
        emptyCellWithBottomBorden.setBorder(Rectangle.BOTTOM);
        emptyCellWithBottomBorden.setColspan(2);

        PdfPCell signatureCell = new PdfPCell(new Paragraph("Firma: " + loanRequest.getUser().getFullName(), fontBold));
        signatureCell.setColspan(2);
        signatureCell.setBorder(Rectangle.NO_BORDER);
        signatureCell.setHorizontalAlignment(Element.ALIGN_CENTER);

        PdfPCell ciCell = new PdfPCell(new Paragraph("Ci: " + loanRequest.getUser().getCi(), fontBold));
        ciCell.setColspan(2);
        ciCell.setBorder(Rectangle.NO_BORDER);
        ciCell.setHorizontalAlignment(Element.ALIGN_CENTER);

        signatureTable.addCell(emptyCell);
        signatureTable.addCell(emptyCellWithBottomBorden);
        signatureTable.addCell(emptyCell);

        signatureTable.addCell(emptyCell);
        signatureTable.addCell(signatureCell);
        signatureTable.addCell(emptyCell);

        signatureTable.addCell(emptyCell);
        signatureTable.addCell(ciCell);
        signatureTable.addCell(emptyCell);

        return signatureTable;
    }

    private PdfPCell createStyledCell(String content, Font font) {
        PdfPCell cell = new PdfPCell(new Paragraph(content, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPaddingTop(5);
        cell.setPaddingBottom(5);
        return cell;
    }
}
