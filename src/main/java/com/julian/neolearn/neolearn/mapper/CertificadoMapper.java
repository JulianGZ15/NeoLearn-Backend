package com.julian.neolearn.neolearn.mapper;

import org.mapstruct.Mapper;

import com.julian.neolearn.neolearn.dto.CertificadoDTO;
import com.julian.neolearn.neolearn.entity.Certificado;

@Mapper(componentModel = "spring")
public interface CertificadoMapper {
    CertificadoDTO toDTO(Certificado entity);
    Certificado toEntity(CertificadoDTO dto);
}
