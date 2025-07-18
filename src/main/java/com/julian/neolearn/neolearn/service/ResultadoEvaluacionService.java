package com.julian.neolearn.neolearn.service;


import java.util.List;
import java.util.Optional;

import com.julian.neolearn.neolearn.dto.ResultadoEvaluacionDTO;
public interface ResultadoEvaluacionService {

    Optional<ResultadoEvaluacionDTO> buscarResultadoEvaluacionPorId(Long cveResultadoevaluacion);
    ResultadoEvaluacionDTO guardarResultadoEvaluacion(ResultadoEvaluacionDTO resultadoevaluacion);
    void borrarResultadoEvaluacionPorId(Long cveResultadoevaluacion);
    List<ResultadoEvaluacionDTO> buscarResultadoEvaluacionPorEvaluacion(Long cveEvaluacion);
}
