package com.julian.neolearn.neolearn.dto;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class MensajeChatDTO {
    private Long cve_mensajeChat;
    private Long cve_claseEnVivo;
    private Long cve_usuario;
    private String contenido;
    private LocalDateTime timestamp;
}

