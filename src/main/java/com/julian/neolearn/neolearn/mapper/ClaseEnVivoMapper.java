package com.julian.neolearn.neolearn.mapper;

import org.mapstruct.Mapper;

import com.julian.neolearn.neolearn.dto.ClaseEnVivoDTO;
import com.julian.neolearn.neolearn.entity.ClaseEnVivo;

@Mapper(componentModel = "spring")
public interface ClaseEnVivoMapper {
    ClaseEnVivoDTO toDTO(ClaseEnVivo entity);
    ClaseEnVivo toEntity(ClaseEnVivoDTO dto);
}

