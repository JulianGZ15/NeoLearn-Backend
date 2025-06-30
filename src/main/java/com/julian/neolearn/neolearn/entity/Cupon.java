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
@Table(name = "Cupon")
public class Cupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cveCupon;

    @ManyToOne
    @JoinColumn(name = "cve_curso")
    private Curso curso;

    private String codigo;

    private BigDecimal descuento_porcentaje;

    private BigDecimal descuento_fijo;

    private LocalDate fecha_inicio;

    private LocalDate fecha_fin;

    private Integer usos_disponibles;
}

