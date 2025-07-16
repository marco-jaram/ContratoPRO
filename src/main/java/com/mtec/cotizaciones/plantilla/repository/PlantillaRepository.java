package com.mtec.cotizaciones.plantilla.repository;

import com.mtec.cotizaciones.plantilla.model.Plantilla;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlantillaRepository extends JpaRepository<Plantilla, Long> {
    List<Plantilla> findByNombreContainingIgnoreCase(String nombre);
}
