package com.julian.neolearn.neolearn.dto;

import lombok.Data;

@Data
public class RespuestaEvaluacionDTO {

    private Long cveRespuestaEvaluacion;
    private Character respuestaUsuario;
    private Long cveResultadoEvaluacion;
    private Long cvePreguntaEvaluacion;
}
