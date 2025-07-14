package com.julian.neolearn.neolearn.service;


import java.util.List;
import java.util.Optional;

import com.julian.neolearn.neolearn.dto.RespuestaDTO;
public interface RespuestaService {

    Optional<RespuestaDTO> buscarRespuestaPorId(Long cveRespuesta);
    //List<RespuestaDTO> listarRespuestas();
    RespuestaDTO guardarRespuesta(RespuestaDTO respuesta, Long cvePregunta);
    void borrarRespuestaPorId(Long cveRespuesta);
    List<RespuestaDTO> listarRespuestasPorPregunta(Long cvePregunta);
}
