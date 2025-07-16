package com.mtec.cotizaciones.common;


import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.mtec.cotizaciones.cotizacion.model.ConceptoPresupuesto;
import com.mtec.cotizaciones.cotizacion.model.Cotizacion;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;

@Service
public class PdfService {

    public ByteArrayInputStream generateCotizacionPdf(Cotizacion cotizacion) throws IOException, DocumentException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, out);

        // --- Fuentes ---
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
        Font boldFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10);
        Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 10);

        document.open();

        // --- Encabezado ---
        Paragraph title = new Paragraph("Cotización de Desarrollo de Software", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);
        document.add(Chunk.NEWLINE);

        // --- Datos de la Cotización y Cliente ---
        PdfPTable infoTable = new PdfPTable(2);
        infoTable.setWidthPercentage(100);

        PdfPCell developerCell = new PdfPCell();
        developerCell.setBorder(Rectangle.NO_BORDER);
        developerCell.addElement(new Paragraph("DE:", headerFont));
        developerCell.addElement(new Paragraph("Marco Jaramillo", boldFont));
        developerCell.addElement(new Paragraph("hola@weblocalmx.com"));
        developerCell.addElement(new Paragraph("664 311 15 77"));

        PdfPCell clientCell = new PdfPCell();
        clientCell.setBorder(Rectangle.NO_BORDER);
        clientCell.addElement(new Paragraph("PARA:", headerFont));
        if (cotizacion.getCliente() != null) { // Comprobación de cliente nulo
            clientCell.addElement(new Paragraph(cotizacion.getCliente().getNombre(), boldFont));
            clientCell.addElement(new Paragraph(cotizacion.getCliente().getEmpresa()));
            clientCell.addElement(new Paragraph(cotizacion.getCliente().getEmail()));
            clientCell.addElement(new Paragraph(cotizacion.getCliente().getTelefono()));
        } else {
            clientCell.addElement(new Paragraph("Cliente no especificado", normalFont));
        }

        infoTable.addCell(developerCell);
        infoTable.addCell(clientCell);
        document.add(infoTable);

        // --- Información general de la cotización (SECCIÓN CORREGIDA) ---
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        Paragraph cotInfo = new Paragraph();
        cotInfo.add(new Chunk("Número de Cotización: ", boldFont));
        cotInfo.add(new Chunk(cotizacion.getId() != null ? cotizacion.getId().toString() : "N/A", normalFont));
        cotInfo.add(Chunk.NEWLINE);
        cotInfo.add(new Chunk("Fecha de Emisión: ", boldFont));

        // Comprobación de fechaCreacion nula
        if (cotizacion.getFechaCreacion() != null) {
            cotInfo.add(new Chunk(cotizacion.getFechaCreacion().format(formatter), normalFont));
        } else {
            cotInfo.add(new Chunk("No especificada", normalFont));
        }
        cotInfo.setSpacingBefore(15);
        document.add(cotInfo);

        // --- Objetivo ---
        document.add(new Paragraph("Objetivo del Proyecto", headerFont));
        // Comprobación de objetivo nulo
        document.add(new Paragraph(cotizacion.getObjetivo() != null ? cotizacion.getObjetivo() : "No especificado", normalFont));
        document.add(Chunk.NEWLINE);

        // --- Presupuesto ---
        document.add(new Paragraph("Desglose del Presupuesto", headerFont));
        PdfPTable budgetTable = new PdfPTable(2);
        budgetTable.setWidthPercentage(100);
        budgetTable.setWidths(new float[]{3f, 1f});
        budgetTable.setSpacingBefore(10);

        Stream.of("Concepto", "Monto").forEach(columnTitle -> {
            PdfPCell header = new PdfPCell();
            header.setBackgroundColor(java.awt.Color.LIGHT_GRAY);
            header.setBorderWidth(1);
            header.setPhrase(new Phrase(columnTitle, boldFont));
            header.setHorizontalAlignment(Element.ALIGN_CENTER);
            budgetTable.addCell(header);
        });

        if (cotizacion.getConceptosPresupuesto() != null && !cotizacion.getConceptosPresupuesto().isEmpty()) {
            for (ConceptoPresupuesto concepto : cotizacion.getConceptosPresupuesto()) {
                budgetTable.addCell(new Phrase(concepto.getConcepto() != null ? concepto.getConcepto() : "", normalFont));

                PdfPCell amountCell = new PdfPCell(new Phrase(String.format("$%,.2f", concepto.getMonto() != null ? concepto.getMonto() : BigDecimal.ZERO), normalFont));
                amountCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                budgetTable.addCell(amountCell);
            }
        }

        PdfPCell totalLabelCell = new PdfPCell(new Phrase("TOTAL (MXN)", boldFont));
        totalLabelCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        totalLabelCell.setColspan(1);
        budgetTable.addCell(totalLabelCell);

        PdfPCell totalValueCell = new PdfPCell(new Phrase(String.format("$%,.2f", cotizacion.getTotal() != null ? cotizacion.getTotal() : BigDecimal.ZERO), boldFont));
        totalValueCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        budgetTable.addCell(totalValueCell);

        document.add(budgetTable);
        document.add(Chunk.NEWLINE);

        // --- Términos y Condiciones ---
        document.add(new Paragraph("Términos y Condiciones", headerFont));
        Paragraph terms = new Paragraph();
        terms.add(new Chunk("Forma de pago: ", boldFont));
        terms.add(new Chunk((cotizacion.getFormaPago() != null ? cotizacion.getFormaPago() : "No especificada") + "\n", normalFont));
        terms.add(new Chunk("Tiempo de entrega estimado: ", boldFont));
        terms.add(new Chunk((cotizacion.getDiasEntrega() != null ? cotizacion.getDiasEntrega().toString() : "N/A") + " días hábiles.\n", normalFont));
        document.add(terms);

        document.close();
        return new ByteArrayInputStream(out.toByteArray());
    }
}