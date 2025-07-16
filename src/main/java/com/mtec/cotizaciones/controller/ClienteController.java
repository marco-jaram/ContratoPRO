package com.mtec.cotizaciones.controller;



import com.mtec.cotizaciones.model.Cliente;
import com.mtec.cotizaciones.service.ClienteService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("clientes", clienteService.findAll());
        return "cliente/list";
    }

    @GetMapping("/nuevo")
    public String newForm(Model model) {
        model.addAttribute("cliente", new Cliente());
        return "cliente/form";
    }

    @PostMapping
    public String save(@ModelAttribute Cliente cliente) {
        clienteService.save(cliente);
        return "redirect:/clientes";
    }

    @GetMapping("/{id}/editar")
    public String editForm(@PathVariable Long id, Model model) {
        Optional<Cliente> cliente = clienteService.findById(id);
        if (cliente.isPresent()) {
            model.addAttribute("cliente", cliente.get());
            return "cliente/form";
        }
        return "redirect:/clientes";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        // Asegúrate de que no haya cotizaciones asociadas antes de borrar, o maneja la lógica de borrado en cascada
        clienteService.deleteById(id);
        return "redirect:/clientes";
    }
}
