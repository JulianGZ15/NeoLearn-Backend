package com.julian.neolearn.neolearn.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.julian.neolearn.neolearn.dto.ConfiguracionDTO;
import com.julian.neolearn.neolearn.entity.ConfiguracionCertificado;

@Mapper(componentModel = "spring")
public interface ConfiguracionMapper {

    @Mapping(source = "empresa.cveEmpresa", target = "cveEmpresa")
    ConfiguracionDTO toDTO(ConfiguracionCertificado configuracionCertificado);

    @Mapping(source = "cveEmpresa", target = "empresa.cveEmpresa")
    ConfiguracionCertificado toEntity(ConfiguracionDTO configuracionDTO);
}
