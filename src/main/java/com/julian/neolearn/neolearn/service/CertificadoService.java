package com.julian.neolearn.neolearn.service;


import java.util.List;
import java.util.Optional;

import com.julian.neolearn.neolearn.dto.CertificadoDTO;
public interface CertificadoService {
    Optional<CertificadoDTO> buscarCertificadoPorId(Long cveCertificado);
    List<CertificadoDTO> listarCertificados();
    CertificadoDTO guardarCertificado(CertificadoDTO dto);
    void borrarCertificadoPorId(Long cveCertificado);
    CertificadoDTO generarCertificado(Long cve_inscripcion) throws Exception;
}
