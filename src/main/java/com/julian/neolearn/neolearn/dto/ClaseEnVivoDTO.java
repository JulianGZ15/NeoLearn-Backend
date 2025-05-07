package com.julian.neolearn.neolearn.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ClaseEnVivoDTO {
    private Long cve_claseEnVivo;
    private Long cve_curso;
    private String titulo;
    private String descripcion;
    private LocalDateTime fecha_programada;
    private Integer duracion_minutos;
    private String url_transmision;
    private Boolean grabacion_disponible;
}
