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
@Table(name = "Comision")
public class Comision {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cve_comision;

    @ManyToOne
    @JoinColumn(name = "cve_curso")
    private Curso curso;

    private BigDecimal porcentaje;

    private BigDecimal monto;

    private LocalDate fecha_generacion;
}

