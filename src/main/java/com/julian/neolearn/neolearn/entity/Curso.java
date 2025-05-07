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
@Table(name = "Curso")
public class Curso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cve_curso")
    private Long cveCurso;

    @ManyToOne
    @JoinColumn(name = "cve_empresa")
    private Empresa empresa;

    private String titulo;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    private BigDecimal precio;

    private Boolean es_gratis;

    @Column(columnDefinition = "TEXT")
    private String publico_objetivo;

    private LocalDate fecha_publicacion;

    @Enumerated(EnumType.STRING)
    private EstadoCurso estado;

    public enum EstadoCurso {
        BORRADOR,
        PUBLICADO,
        INACTIVO
    }
}

