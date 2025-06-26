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
@Table(name = "clase_en_vivo")
public class ClaseEnVivo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cve_claseEnVivo")
    private Long cveClaseEnVivo;

    @Column(nullable = false)
    private String titulo;

    private String descripcion;

    @Column(nullable = false)
    private LocalDateTime fechaProgramada;

    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cve_salaEnVivo")
    private SalaEnVivo salaEnVivo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cve_usuario")
    private Usuario instructor;

    @Enumerated(EnumType.STRING)
    private EstadoClase estado = EstadoClase.PROGRAMADA;

    private Boolean finalizada = false;

    private Integer duracionEstimadaMinutos;


    public enum EstadoClase {
        PROGRAMADA,
        EN_VIVO,
        FINALIZADA,
        CANCELADA
    }

}