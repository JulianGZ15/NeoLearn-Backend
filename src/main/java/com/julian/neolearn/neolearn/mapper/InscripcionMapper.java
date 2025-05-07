package com.julian.neolearn.neolearn.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.julian.neolearn.neolearn.dto.InscripcionDTO;
import com.julian.neolearn.neolearn.entity.Inscripcion;

@Mapper(componentModel = "spring")
public interface InscripcionMapper {
    @Mapping(source = "curso.cveCurso", target = "cveCurso")
    @Mapping(source = "usuario.cveUsuario", target = "cveUsuario")
    InscripcionDTO toDTO(Inscripcion entity);
    
    @Mapping(source = "cveCurso", target = "curso.cveCurso")
    @Mapping(source = "cveUsuario", target = "usuario.cveUsuario")
    Inscripcion toEntity(InscripcionDTO dto);
}
