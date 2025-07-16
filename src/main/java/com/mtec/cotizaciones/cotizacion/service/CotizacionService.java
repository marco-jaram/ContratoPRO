package com.mtec.cotizaciones.cotizacion.service;

import com.mtec.cotizaciones.cotizacion.model.ConceptoPresupuesto;
import com.mtec.cotizaciones.cotizacion.model.Cotizacion;
import com.mtec.cotizaciones.cliente.repository.ClienteRepository;
import com.mtec.cotizaciones.cotizacion.repository.CotizacionRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CotizacionService {

    private final CotizacionRepository cotizacionRepository;
    private final ClienteRepository clienteRepository;

    public CotizacionService(CotizacionRepository cotizacionRepository, ClienteRepository clienteRepository) {
        this.cotizacionRepository = cotizacionRepository;
        this.clienteRepository = clienteRepository;
    }

    public List<Cotizacion> findAll() {
        return cotizacionRepository.findAll();
    }

    public Optional<Cotizacion> findById(Long id) {
        return cotizacionRepository.findById(id);
    }

    public Cotizacion save(Cotizacion cotizacion) {
        calcularTotal(cotizacion);
        return cotizacionRepository.save(cotizacion);
    }

    public void deleteById(Long id) {
        cotizacionRepository.deleteById(id);
    }

    public List<Cotizacion> findByCliente(Long clienteId) {
        return cotizacionRepository.findByClienteId(clienteId);
    }

    public Cotizacion crearDesdePlantilla(Long plantillaId, Long clienteId) {
        // Implementar lógica para crear cotización desde plantilla
        return new Cotizacion();
    }

    private void calcularTotal(Cotizacion cotizacion) {
        BigDecimal total = cotizacion.getConceptosPresupuesto().stream()
                .map(ConceptoPresupuesto::getMonto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        cotizacion.setTotal(total);
    }
}
