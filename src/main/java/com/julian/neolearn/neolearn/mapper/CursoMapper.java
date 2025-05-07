package com.julian.neolearn.neolearn.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.julian.neolearn.neolearn.dto.CursoDTO;
import com.julian.neolearn.neolearn.entity.Curso;

@Mapper(componentModel = "spring")
public interface CursoMapper {
    @Mapping(source = "empresa.cveEmpresa", target = "cveEmpresa")
    CursoDTO toDTO(Curso entity);
    @Mapping(source = "cveEmpresa", target = "empresa.cveEmpresa")
    Curso toEntity(CursoDTO dto);
}
