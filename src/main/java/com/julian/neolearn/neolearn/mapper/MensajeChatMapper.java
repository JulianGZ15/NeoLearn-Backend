package com.julian.neolearn.neolearn.mapper;

import org.mapstruct.Mapper;

import com.julian.neolearn.neolearn.dto.MensajeChatDTO;
import com.julian.neolearn.neolearn.entity.MensajeChat;

@Mapper(componentModel = "spring")
public interface MensajeChatMapper {
    MensajeChatDTO toDTO(MensajeChat entity);
    MensajeChat toEntity(MensajeChatDTO dto);
}

