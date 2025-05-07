package com.julian.neolearn.neolearn.service;


import java.util.List;
import java.util.Optional;

import com.julian.neolearn.neolearn.dto.EvaluacionDTO;
public interface EvaluacionService {

     Optional<EvaluacionDTO> buscarEvaluacionPorId(Long cveEvaluacion);
     //List<EvaluacionDTO> listarEvaluaciones();
     EvaluacionDTO guardarEvaluacion(EvaluacionDTO evaluacion, Long cveCurso);
     void borrarEvaluacionPorId(Long cveEvaluacion);
     List<EvaluacionDTO> listarEvaluacionesPorCurso(Long cveCurso);
}
