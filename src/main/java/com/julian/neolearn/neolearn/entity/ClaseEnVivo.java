package com.julian.neolearn.neolearn.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ClaseEnVivo")
public class ClaseEnVivo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cve_claseEnVivo;

    @ManyToOne
    @JoinColumn(name = "cve_curso")
    private Curso curso;

    private String titulo;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    private LocalDateTime fecha_programada;

    private Integer duracion_minutos;

    @Column(columnDefinition = "TEXT")
    private String url_transmision;

    private Boolean grabacion_disponible;
}
