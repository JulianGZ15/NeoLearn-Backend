package com.julian.neolearn.neolearn.mapper;

import org.mapstruct.Mapper;

import com.julian.neolearn.neolearn.dto.CuponDTO;
import com.julian.neolearn.neolearn.entity.Cupon;

@Mapper(componentModel = "spring")
public interface CuponMapper {
    CuponDTO toDTO(Cupon entity);
    Cupon toEntity(CuponDTO dto);
}

