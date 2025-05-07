package com.julian.neolearn.neolearn.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.julian.neolearn.neolearn.dto.RespuestaDTO;
import com.julian.neolearn.neolearn.entity.Respuesta;

@Mapper(componentModel = "spring")
public interface RespuestaMapper {
    @Mapping(source = "pregunta.cvePregunta", target = "cvePregunta")
    @Mapping(source = "usuario.cveUsuario", target = "cveUsuario")
    RespuestaDTO toDTO(Respuesta entity);
    
    @Mapping(source = "cvePregunta", target = "pregunta.cvePregunta")
    @Mapping(source = "cveUsuario", target = "usuario.cveUsuario")
    Respuesta toEntity(RespuestaDTO dto);
}
