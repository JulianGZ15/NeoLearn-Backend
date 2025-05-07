package com.julian.neolearn.neolearn.service;


import java.util.List;
import java.util.Optional;

import com.julian.neolearn.neolearn.dto.InscripcionDTO;
public interface InscripcionService {

     Optional<InscripcionDTO> buscarInscripcionPorId(Long cveInscripcion);
     //List<InscripcionDTO> listarInscripciones();
     InscripcionDTO guardarInscripcion(InscripcionDTO inscripcion, Long cveCurso);
     void borrarInscripcionPorId(Long cveInscripcion);
     List<InscripcionDTO> listarInscripcionesPorCurso(Long cveCurso);
}
