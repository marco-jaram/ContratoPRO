package com.mtec.cotizaciones.plantilla.service;

import com.mtec.cotizaciones.plantilla.model.Plantilla;
import com.mtec.cotizaciones.plantilla.repository.PlantillaRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PlantillaService {

    private final PlantillaRepository plantillaRepository;

    public PlantillaService(PlantillaRepository plantillaRepository) {
        this.plantillaRepository = plantillaRepository;
    }

    public List<Plantilla> findAll() {
        return plantillaRepository.findAll();
    }

    public Optional<Plantilla> findById(Long id) {
        return plantillaRepository.findById(id);
    }

    public Plantilla save(Plantilla plantilla) {
        return plantillaRepository.save(plantilla);
    }

    public void deleteById(Long id) {
        plantillaRepository.deleteById(id);
    }
}