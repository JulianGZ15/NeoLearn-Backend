package com.julian.neolearn.neolearn.dto;

import lombok.Data;

@Data
public class VideoDTO {
    private Long cveVideo;
    private Long cveCurso;
    private String titulo;
    private String url;
    private Integer duracion_minutos;
    private Integer orden;
    private String portada;
}

