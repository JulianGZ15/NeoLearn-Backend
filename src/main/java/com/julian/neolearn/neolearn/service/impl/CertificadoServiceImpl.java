package com.julian.neolearn.neolearn.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.julian.neolearn.neolearn.dto.CertificadoDTO;
import com.julian.neolearn.neolearn.entity.Certificado;
import com.julian.neolearn.neolearn.mapper.CertificadoMapper;
import com.julian.neolearn.neolearn.repository.CertificadoRepository;
import com.julian.neolearn.neolearn.service.CertificadoService;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;
import java.util.Optional;

@Service
public class CertificadoServiceImpl implements CertificadoService {

    @Autowired
    private CertificadoRepository certificadoRepository;
    @Autowired
    private CertificadoMapper certificadoMapper;

    @Override
    public Optional<CertificadoDTO> buscarCertificadoPorId(Long cveCertificado) {
        return certificadoRepository.findById(cveCertificado).map(certificadoMapper::toDTO);
    }

    @Override
    public List<CertificadoDTO> listarCertificados() {
        return certificadoRepository.findAll().stream().map(certificadoMapper::toDTO).toList();
    }

    public CertificadoDTO guardarCertificado(CertificadoDTO dto) {
        Certificado Certificado = certificadoMapper.toEntity(dto);

        if (dto.getCve_certificado() != null) {
            // Validar existencia previa
            Optional<Certificado> existente = certificadoRepository.findById(dto.getCve_certificado());
            if (existente.isEmpty()) {
                throw new EntityNotFoundException("Certificado no encontrada con ID: " + dto.getCve_certificado());
            }
        }

        Certificado = certificadoRepository.save(Certificado);
        return certificadoMapper.toDTO(Certificado);
    }

    @Override
    public void borrarCertificadoPorId(Long cveCertificado) {

        certificadoRepository.deleteById(cveCertificado);
    }

    
    

}
