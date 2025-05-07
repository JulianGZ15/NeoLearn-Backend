package com.julian.neolearn.neolearn.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.julian.neolearn.neolearn.dto.PreguntaEvaluacionDTO;
import com.julian.neolearn.neolearn.entity.PreguntaEvaluacion;

@Mapper(componentModel = "spring")
public interface PreguntaEvaluacionMapper {
    @Mapping(target = "cveEvaluacion", source = "evaluacion.cveEvaluacion")
    PreguntaEvaluacionDTO toDTO(PreguntaEvaluacion entity);
    
    @Mapping(target = "evaluacion.cveEvaluacion", source = "cveEvaluacion")
    PreguntaEvaluacion toEntity(PreguntaEvaluacionDTO dto);
}

