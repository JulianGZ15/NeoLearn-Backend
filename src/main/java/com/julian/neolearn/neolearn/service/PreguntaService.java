package com.julian.neolearn.neolearn.service;


import java.util.List;
import java.util.Optional;

import com.julian.neolearn.neolearn.dto.PreguntaDTO;
public interface PreguntaService {

    Optional<PreguntaDTO> buscarPreguntaPorId(Long cvePregunta);
    //List<PreguntaDTO> listarPreguntas();
    PreguntaDTO guardarPregunta(PreguntaDTO pregunta, Long cveCurso);
    void borrarPreguntaPorId(Long cvePregunta);
    List<PreguntaDTO> listarPreguntasPorCurso(Long cveCurso);
}
