package com.mtec.cotizaciones.cotizacion.repository;

import com.mtec.cotizaciones.cotizacion.model.Cotizacion;
import com.mtec.cotizaciones.cotizacion.model.EstadoCotizacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CotizacionRepository extends JpaRepository<Cotizacion, Long> {
    List<Cotizacion> findByClienteId(Long clienteId);
    List<Cotizacion> findByEstado(EstadoCotizacion estado);
    List<Cotizacion> findByFechaCreacionBetween(LocalDateTime inicio, LocalDateTime fin);
}
