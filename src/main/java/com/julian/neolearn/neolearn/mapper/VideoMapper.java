package com.julian.neolearn.neolearn.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.julian.neolearn.neolearn.dto.VideoDTO;
import com.julian.neolearn.neolearn.entity.Video;

@Mapper(componentModel = "spring")
public interface VideoMapper {
    @Mapping(target = "cveVideo", source = "entity.cveVideo")
    VideoDTO toDTO(Video entity);
    @Mapping(target = "cveVideo", source = "dto.cveVideo")
    Video toEntity(VideoDTO dto);
}
