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
@Table(name = "ReporteCurso")
public class ReporteCurso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cve_reporteCurso;

    @ManyToOne
    @JoinColumn(name = "cve_curso")
    private Curso curso;

    private LocalDate fecha;

    private Integer vistas;

    private Integer inscritos;

    private BigDecimal ingresos_generados;
}
