package com.julian.neolearn.neolearn.mapper;

import org.mapstruct.Mapper;

import com.julian.neolearn.neolearn.dto.NotificacionDTO;
import com.julian.neolearn.neolearn.entity.Notificacion;

@Mapper(componentModel = "spring")
public interface NotificacionMapper {
    NotificacionDTO toDTO(Notificacion entity);
    Notificacion toEntity(NotificacionDTO dto);
}

