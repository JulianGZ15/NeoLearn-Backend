package com.julian.neolearn.neolearn.dto;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class NotificacionDTO {
    private Long cve_notificacion;
    private Long cve_usuario;
    private String mensaje;
    private Boolean leido;
    private LocalDateTime fecha_envio;
}
