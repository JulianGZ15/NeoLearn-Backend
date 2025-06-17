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
@Table(name = "Respuesta")
public class Respuesta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cveRespuesta;

    @ManyToOne
    @JoinColumn(name = "cve_pregunta")
    private Pregunta pregunta;

    @ManyToOne
    @JoinColumn(name = "cve_usuario")
    private Usuario usuario;

    @Column(columnDefinition = "TEXT")
    private String contenido;

    private LocalDateTime fecha;

    @PrePersist
    private void prePersist() {
        if (fecha == null) {
            fecha = LocalDateTime.now();
        }
    }
}
