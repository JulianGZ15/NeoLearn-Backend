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
@Table(name = "MensajeChat")
public class MensajeChat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cve_mensajeChat;

    @ManyToOne
    @JoinColumn(name = "cve_claseEnVivo")
    private ClaseEnVivo claseEnVivo;

    @ManyToOne
    @JoinColumn(name = "cve_usuario")
    private Usuario usuario;

    @Column(columnDefinition = "TEXT")
    private String contenido;

    private LocalDateTime timestamp;
}
