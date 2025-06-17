package com.julian.neolearn.neolearn.service;


import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Optional;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import com.julian.neolearn.neolearn.dto.CursoDTO;
public interface CursoService {

    Optional<CursoDTO> buscarCursoPorId(Long cveCurso);
    List<CursoDTO> listarCursos();
    CursoDTO guardarCurso(CursoDTO curso);
    void borrarCursoPorId(Long cveCurso);
    CursoDTO guardarPortada(Long cursoId, MultipartFile file) throws IOException;
    Resource obtenerPortada(String nombreArchivo) throws MalformedURLException;

    
}
