package com.julian.neolearn.neolearn.mapper;

import org.mapstruct.Mapper;

import com.julian.neolearn.neolearn.dto.ComisionDTO;
import com.julian.neolearn.neolearn.entity.Comision;

@Mapper(componentModel = "spring")
public interface ComisionMapper {
    ComisionDTO toDTO(Comision entity);
    Comision toEntity(ComisionDTO dto);
}

