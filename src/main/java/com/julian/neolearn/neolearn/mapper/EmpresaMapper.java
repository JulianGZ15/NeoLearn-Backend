package com.julian.neolearn.neolearn.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.julian.neolearn.neolearn.dto.EmpresaDTO;
import com.julian.neolearn.neolearn.entity.Empresa;

@Mapper(componentModel = "spring")
public interface EmpresaMapper {
    EmpresaDTO toDTO(Empresa entity);

    Empresa toEntity(EmpresaDTO dto);
}
