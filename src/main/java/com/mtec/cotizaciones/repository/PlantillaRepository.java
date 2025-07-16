package com.mtec.cotizaciones.repository;

import com.mtec.cotizaciones.model.Plantilla;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlantillaRepository extends JpaRepository<Plantilla, Long> {
    List<Plantilla> findByNombreContainingIgnoreCase(String nombre);
}
