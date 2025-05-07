package com.julian.neolearn.neolearn.dto;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class RespuestaDTO {
    private Long cveRespuesta;
    private Long cvePregunta;
    private Long cveUsuario;
    private String contenido;
    private LocalDateTime fecha;
}

