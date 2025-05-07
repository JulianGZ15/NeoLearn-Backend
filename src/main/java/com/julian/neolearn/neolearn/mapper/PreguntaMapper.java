package com.julian.neolearn.neolearn.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.julian.neolearn.neolearn.dto.PreguntaDTO;
import com.julian.neolearn.neolearn.entity.Pregunta;

@Mapper(componentModel = "spring")
public interface PreguntaMapper {

    @Mapping(source = "curso.cveCurso", target = "cveCurso")
    @Mapping(source = "usuario.cveUsuario", target = "cveUsuario")
    PreguntaDTO toDTO(Pregunta entity);
    @Mapping(source = "cveCurso", target = "curso.cveCurso")
    @Mapping(source = "cveUsuario", target = "usuario.cveUsuario")
    Pregunta toEntity(PreguntaDTO dto);
}

