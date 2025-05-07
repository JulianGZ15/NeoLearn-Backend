package com.julian.neolearn.neolearn.mapper;

import org.mapstruct.Mapper;

import com.julian.neolearn.neolearn.dto.ReporteCursoDTO;
import com.julian.neolearn.neolearn.entity.ReporteCurso;

@Mapper(componentModel = "spring")
public interface ReporteCursoMapper {
    ReporteCursoDTO toDTO(ReporteCurso entity);
    ReporteCurso toEntity(ReporteCursoDTO dto);
}
