package com.mtec.cotizaciones.cotizacion.model;

import com.mtec.cotizaciones.cliente.model.Cliente;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cotizacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    @ToString.Exclude
    private Cliente cliente;

    private String desarrollador;
    private String contactoDeveloper;
    private String celDeveloper;
    private String emailDeveloper;

    private String objetivo;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private BigDecimal total;
    private String moneda = "MXN";

    @OneToMany(mappedBy = "cotizacion", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Caracteristica> caracteristicas = new ArrayList<>();

    @OneToMany(mappedBy = "cotizacion", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<ConceptoPresupuesto> conceptosPresupuesto = new ArrayList<>();

    // Términos y condiciones
    private String formaPago;
    private Integer porcentajeAnticipo = 50;
    private Integer revisiones = 2;
    private Integer reuniones = 2;
    private String metodoPago;
    private Integer diasEntrega = 20;

    @Enumerated(EnumType.STRING)
    private EstadoCotizacion estado = EstadoCotizacion.BORRADOR;

    @CreationTimestamp
    private LocalDateTime fechaCreacion;

    @UpdateTimestamp
    private LocalDateTime fechaActualizacion;
}
