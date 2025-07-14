package com.julian.neolearn.neolearn.service;

import java.util.List;

import com.julian.neolearn.neolearn.dto.RespuestaEvaluacionDTO;

public interface RespuestasEvaluacionService {

    RespuestaEvaluacionDTO guardarRespuestaEvaluacion (RespuestaEvaluacionDTO respuestaEvaluacion);
    List<RespuestaEvaluacionDTO> guardarRespuestasEvaluacion (List<RespuestaEvaluacionDTO> respuestasEvaluacion);
    List<RespuestaEvaluacionDTO> buscarRespuestasPorResultadoEvaluacion(Long cveResultadoEvaluacion);
    void borrarRespuestasEvaluacionPoResultadoEvaluacion(Long cveResultadoEvaluacion);
}
