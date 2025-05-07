package com.julian.neolearn.neolearn.dto;

import lombok.Data;

@Data
public class PreguntaEvaluacionDTO {
    private Long cvePreguntaEvaluacion;
    private Long cveEvaluacion;
    private String pregunta;
    private String opcion_a;
    private String opcion_b;
    private String opcion_c;
    private String opcion_d;
    private Character respuesta_correcta;
}

