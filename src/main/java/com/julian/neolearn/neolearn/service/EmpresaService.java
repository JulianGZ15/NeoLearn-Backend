package com.julian.neolearn.neolearn.service;


import java.util.List;
import java.util.Optional;

import com.julian.neolearn.neolearn.dto.EmpresaDTO;
public interface EmpresaService {
    Optional<EmpresaDTO> buscarEmpresaPorId(Long cveEmpresa);
    List<EmpresaDTO> listarEmpresas();
    EmpresaDTO guardarEmpresa(EmpresaDTO empresa);
    void borrarEmpresaPorId(Long cveEmpresa);
    
    List<EmpresaDTO> buscarEmpresaPorUsuario(Long cveUsuario);

}
