package com.mtec.cotizaciones.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Plantilla {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String descripcion;

    @ElementCollection
    @CollectionTable(name = "plantilla_caracteristicas")
    private List<String> caracteristicasDefault = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "plantilla_conceptos")
    private List<String> conceptosDefault = new ArrayList<>();

    @CreationTimestamp
    private LocalDateTime fechaCreacion;
}
