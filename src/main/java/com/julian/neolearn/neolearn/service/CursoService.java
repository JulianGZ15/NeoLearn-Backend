package com.julian.neolearn.neolearn.service;


import java.util.List;
import java.util.Optional;

import com.julian.neolearn.neolearn.dto.CursoDTO;
public interface CursoService {

    Optional<CursoDTO> buscarCursoPorId(Long cveCurso);
    List<CursoDTO> listarCursos();
    CursoDTO guardarCurso(CursoDTO curso);
    void borrarCursoPorId(Long cveCurso);
    
}
