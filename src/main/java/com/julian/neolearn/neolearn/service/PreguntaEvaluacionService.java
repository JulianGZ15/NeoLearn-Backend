package com.julian.neolearn.neolearn.service;


import java.util.List;
import java.util.Optional;

import com.julian.neolearn.neolearn.dto.PreguntaEvaluacionDTO;
public interface PreguntaEvaluacionService {

     Optional<PreguntaEvaluacionDTO> buscarPreguntaEvaluacionPorId(Long cvePreguntaEvaluacion);
     PreguntaEvaluacionDTO guardarPreguntaEvaluacion(PreguntaEvaluacionDTO preguntaEvaluacion, Long cveEvaluacion);
     void borrarPreguntaEvaluacionPorId(Long cvePreguntaEvaluacion);
     List<PreguntaEvaluacionDTO> listarPreguntasPorEvaluacion(Long cveEvaluacion);
}
