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
@Table(name = "mensaje_chat")
public class MensajeChat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cve_mensajeChat")
    private Long cveMensajeChat;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cve_claseEnVivo", nullable = false)
    private ClaseEnVivo claseEnVivo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cve_usuario", nullable = false)
    private Usuario usuario;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String contenido;

    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;

    @Column(name = "tipo_mensaje")
    @Enumerated(EnumType.STRING)
    private TipoMensaje tipoMensaje = TipoMensaje.TEXTO;

    @Column(name = "editado")
    private Boolean editado = false;

    @Column(name = "fecha_edicion")
    private LocalDateTime fechaEdicion;

    // Constructor para inicializar timestamp

    @PrePersist
    public void prePersist() {
        this.timestamp = LocalDateTime.now();
    }

     public enum TipoMensaje {
        TEXTO,
        SISTEMA,
        ARCHIVO,
        EMOJI
    }
   
}
