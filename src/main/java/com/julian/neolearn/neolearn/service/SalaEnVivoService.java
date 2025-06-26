package com.julian.neolearn.neolearn.service;

import java.util.List;

import com.julian.neolearn.neolearn.dto.SalaEnVivoDTO;

public interface SalaEnVivoService {
    SalaEnVivoDTO crearSala(Long idCurso);

    SalaEnVivoDTO obtenerPorId(Long idSala);

    List<SalaEnVivoDTO> obtenerPorCurso(Long idCurso);

    void eliminarSala(Long idSala);
}
