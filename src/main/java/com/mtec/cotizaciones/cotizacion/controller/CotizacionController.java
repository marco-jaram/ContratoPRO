package com.mtec.cotizaciones.cotizacion.controller;

import com.lowagie.text.DocumentException;
import com.mtec.cotizaciones.cotizacion.model.Cotizacion;
import com.mtec.cotizaciones.cliente.service.ClienteService;
import com.mtec.cotizaciones.cotizacion.service.CotizacionService;
import com.mtec.cotizaciones.common.PdfService;
import com.mtec.cotizaciones.plantilla.service.PlantillaService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import com.mtec.cotizaciones.cotizacion.model.Caracteristica;
import com.mtec.cotizaciones.cotizacion.model.ConceptoPresupuesto;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Optional;

@Controller
@RequestMapping("/cotizaciones")
public class CotizacionController {

    private final CotizacionService cotizacionService;
    private final ClienteService clienteService;
    private final PlantillaService plantillaService;
    private final PdfService pdfService;

    public CotizacionController(CotizacionService cotizacionService, ClienteService clienteService, PlantillaService plantillaService, PdfService pdfService) {
        this.cotizacionService = cotizacionService;
        this.clienteService = clienteService;
        this.plantillaService = plantillaService;
        this.pdfService = pdfService;
    }

    // ... los métodos list, show, delete no cambian ...
    @GetMapping
    public String list(Model model) {
        model.addAttribute("cotizaciones", cotizacionService.findAll());
        return "cotizacion/list";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable Long id, Model model) {
        Optional<Cotizacion> cotizacion = cotizacionService.findById(id);
        if (cotizacion.isPresent()) {
            model.addAttribute("cotizacion", cotizacion.get());
            return "cotizacion/preview";
        }
        return "redirect:/cotizaciones";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        cotizacionService.deleteById(id);
        return "redirect:/cotizaciones";
    }

    @GetMapping("/nueva")
    public String newCotizacion(Model model) {
        model.addAttribute("cotizacion", new Cotizacion());
        model.addAttribute("clientes", clienteService.findAll());
        model.addAttribute("plantillas", plantillaService.findAll());
        return "cotizacion/form";
    }

    @GetMapping("/{id}/editar")
    public String edit(@PathVariable Long id, Model model) {
        Optional<Cotizacion> cotizacion = cotizacionService.findById(id);
        if (cotizacion.isPresent()) {
            model.addAttribute("cotizacion", cotizacion.get());
            model.addAttribute("clientes", clienteService.findAll());
            model.addAttribute("plantillas", plantillaService.findAll());
            return "cotizacion/form";
        }
        return "redirect:/cotizaciones";
    }

    // MÉTODO SAVE MEJORADO
    @PostMapping
    public String save(@ModelAttribute Cotizacion cotizacion) {
        // Establecer la relación bidireccional antes de guardar
        if (cotizacion.getCaracteristicas() != null) {
            for (Caracteristica caracteristica : cotizacion.getCaracteristicas()) {
                caracteristica.setCotizacion(cotizacion);
            }
        }
        if (cotizacion.getConceptosPresupuesto() != null) {
            for (ConceptoPresupuesto concepto : cotizacion.getConceptosPresupuesto()) {
                concepto.setCotizacion(cotizacion);
            }
        }

        cotizacionService.save(cotizacion);
        return "redirect:/cotizaciones/" + cotizacion.getId(); // Redirige a la vista previa
    }
    @GetMapping(value = "/{id}/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> generatePdf(@PathVariable Long id) throws IOException, DocumentException {
        Optional<Cotizacion> cotizacionOpt = cotizacionService.findById(id);

        if (cotizacionOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Cotizacion cotizacion = cotizacionOpt.get();
        ByteArrayInputStream bis = pdfService.generateCotizacionPdf(cotizacion);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=cotizacion-" + cotizacion.getId() + ".pdf");

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }

}