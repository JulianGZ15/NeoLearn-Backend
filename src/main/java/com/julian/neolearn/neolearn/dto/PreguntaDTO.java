package com.julian.neolearn.neolearn.dto;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class PreguntaDTO {
    private Long cvePregunta;
    private Long cveCurso;
    private Long cveUsuario;
    private String contenido;
    private LocalDateTime fecha;
}

