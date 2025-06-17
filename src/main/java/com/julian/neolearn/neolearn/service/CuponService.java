package com.julian.neolearn.neolearn.service;

import java.util.List;
import java.util.Optional;

import com.julian.neolearn.neolearn.dto.CuponDTO;

public interface CuponService {

     Optional<CuponDTO> buscarCuponPorId(Long cveCupon);
     List<CuponDTO> listarCupones(Long cve_curso);
     CuponDTO guardarCupon(CuponDTO cupon, Long cveCurso);
     void borrarCuponPorId(Long cveCupon);

}
