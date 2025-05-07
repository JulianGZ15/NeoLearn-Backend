package com.julian.neolearn.neolearn.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Inscripcion")
public class Inscripcion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cveInscripcion;

    @ManyToOne
    @JoinColumn(name = "cve_usuario")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "cve_curso")
    private Curso curso;

    private LocalDate fecha_inscripcion;

    private BigDecimal precio_pagado;

    private String metodo_pago;

    @Enumerated(EnumType.STRING)
    private EstadoInscripcion estado;

    public enum EstadoInscripcion {
        ACTIVA,
        CANCELADA,
        FINALIZADA
    }
}
