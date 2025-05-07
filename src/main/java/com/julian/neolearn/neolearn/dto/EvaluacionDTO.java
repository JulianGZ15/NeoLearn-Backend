package com.julian.neolearn.neolearn.dto;

import lombok.Data;

@Data
public class EvaluacionDTO {
    private Long cveEvaluacion;
    private Long cveCurso;
    private String titulo;
    private Integer duracion_minutos;
}

