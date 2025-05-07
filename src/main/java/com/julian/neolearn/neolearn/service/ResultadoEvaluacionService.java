package com.julian.neolearn.neolearn.service;


import java.util.List;

import com.julian.neolearn.neolearn.dto.ResultadoEvaluacionDTO;
public interface ResultadoEvaluacionService {

    //Optional<ResultadoEvaluacionDTO> buscarResultadoEvaluacionPorId(Long cveResultadoevaluacion);
    //List<ResultadoEvaluacionDTO> listarResultadoEvaluaciones();
    ResultadoEvaluacionDTO guardarResultadoEvaluacion(ResultadoEvaluacionDTO resultadoevaluacion);
    void borrarResultadoEvaluacionPorId(Long cveResultadoevaluacion);
    List<ResultadoEvaluacionDTO> buscarResultadoEvaluacionPorEvaluacion(Long cveEvaluacion);
}
