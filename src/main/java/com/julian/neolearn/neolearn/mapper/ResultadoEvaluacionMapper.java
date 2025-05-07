package com.julian.neolearn.neolearn.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.julian.neolearn.neolearn.dto.ResultadoEvaluacionDTO;
import com.julian.neolearn.neolearn.entity.ResultadoEvaluacion;

@Mapper(componentModel = "spring")
public interface ResultadoEvaluacionMapper {
    @Mapping(target = "cveEvaluacion", source = "evaluacion.cveEvaluacion")
    @Mapping(target = "cveUsuario", source = "usuario.cveUsuario")
    ResultadoEvaluacionDTO toDTO(ResultadoEvaluacion entity);

    @Mapping(target = "evaluacion.cveEvaluacion", source = "cveEvaluacion")
    @Mapping(target = "usuario.cveUsuario", source = "cveUsuario")
    ResultadoEvaluacion toEntity(ResultadoEvaluacionDTO dto);
}

