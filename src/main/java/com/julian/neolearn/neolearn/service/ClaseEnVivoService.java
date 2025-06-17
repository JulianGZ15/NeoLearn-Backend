package com.julian.neolearn.neolearn.service;


import java.util.List;
import java.util.Optional;

import com.julian.neolearn.neolearn.dto.ClaseEnVivoDTO;

public interface ClaseEnVivoService {
    Optional<ClaseEnVivoDTO> buscarClaseEnVivoPorId(Long cveClaseEnVivo);
    List<ClaseEnVivoDTO> listarClasesEnVivo(Long cveCurso);
    ClaseEnVivoDTO guardarClaseEnVivo(ClaseEnVivoDTO claseEnVivo);
    void borrarClaseEnVivoPorId(Long cveClaseEnVivo);
    
}
