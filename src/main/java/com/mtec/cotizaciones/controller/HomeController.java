package com.mtec.cotizaciones.controller;

import com.mtec.cotizaciones.service.CotizacionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final CotizacionService cotizacionService;

    public HomeController(CotizacionService cotizacionService) {
        this.cotizacionService = cotizacionService;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("cotizaciones", cotizacionService.findAll());
        return "index";
    }
}