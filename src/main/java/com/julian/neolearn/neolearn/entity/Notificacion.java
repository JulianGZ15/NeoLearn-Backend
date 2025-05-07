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
@Table(name = "Notificacion")
public class Notificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cve_notificacion;

    @ManyToOne
    @JoinColumn(name = "cve_usuario")
    private Usuario usuario;

    @Column(columnDefinition = "TEXT")
    private String mensaje;

    private Boolean leido;

    private LocalDateTime fecha_envio;
}

