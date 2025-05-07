package com.julian.neolearn.neolearn.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.julian.neolearn.neolearn.dto.EvaluacionDTO;
import com.julian.neolearn.neolearn.entity.Evaluacion;

@Mapper(componentModel = "spring")
public interface EvaluacionMapper {
    @Mapping(target = "cveCurso", source = "curso.cveCurso")
    EvaluacionDTO toDTO(Evaluacion entity);

    @Mapping(target = "curso.cveCurso", source = "cveCurso")
    Evaluacion toEntity(EvaluacionDTO dto);
}
