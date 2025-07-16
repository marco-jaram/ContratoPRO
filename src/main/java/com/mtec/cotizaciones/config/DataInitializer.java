package com.mtec.cotizaciones.config;

import com.mtec.cotizaciones.model.Cliente;
import com.mtec.cotizaciones.model.Plantilla;
import com.mtec.cotizaciones.service.ClienteService;
import com.mtec.cotizaciones.service.PlantillaService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class DataInitializer implements CommandLineRunner {

    private final ClienteService clienteService;
    private final PlantillaService plantillaService;

    public DataInitializer(ClienteService clienteService, PlantillaService plantillaService) {
        this.clienteService = clienteService;
        this.plantillaService = plantillaService;
    }

    @Override
    public void run(String... args) throws Exception {
        // Crear datos de prueba
        if (clienteService.findAll().isEmpty()) {
            Cliente cliente = new Cliente();
            cliente.setNombre("Abdiel Ortega");
            cliente.setEmpresa("La Linea");
            cliente.setContacto("664 188 8387");
            clienteService.save(cliente);

            Plantilla plantilla = new Plantilla();
            plantilla.setNombre("Desarrollo Web WordPress");
            plantilla.setDescripcion("Plantilla para desarrollo web con WordPress");
            plantilla.getCaracteristicasDefault().addAll(Arrays.asList(
                    "Diseño responsivo y adaptable a dispositivos móviles",
                    "Creación y desarrollo de un theme original en PHP",
                    "Desarrollo de templates personalizados"
            ));
            plantilla.getConceptosDefault().addAll(Arrays.asList(
                    "Investigación y Planificación",
                    "Diseño del Theme",
                    "Desarrollo",
                    "Pruebas",
                    "Implementación y Lanzamiento"
            ));
            plantillaService.save(plantilla);
        }
    }
}