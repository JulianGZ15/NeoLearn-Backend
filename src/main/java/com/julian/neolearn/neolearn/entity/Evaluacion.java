package com.julian.neolearn.neolearn.entity;

import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Evaluacion")
public class Evaluacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cveEvaluacion;

    @ManyToOne
    @JoinColumn(name = "cve_curso")
    private Curso curso;

    private String titulo;

    private Integer duracion_minutos;
}
