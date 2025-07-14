package com.julian.neolearn.neolearn.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.julian.neolearn.neolearn.dto.RespuestaEvaluacionDTO;
import com.julian.neolearn.neolearn.entity.RespuestaEvaluacion;

@Mapper(componentModel = "spring")
public interface RespuestaEvaluacionMapper {
    @Mapping(source = "cveResultadoEvaluacion", target = "resultadoEvaluacion.cveResultadoEvaluacion")
    @Mapping(source = "cvePreguntaEvaluacion", target = "preguntaEvaluacion.cvePreguntaEvaluacion")
    RespuestaEvaluacion toEntity (RespuestaEvaluacionDTO dto);

    @Mapping(source = "resultadoEvaluacion.cveResultadoEvaluacion", target = "cveResultadoEvaluacion")
    @Mapping(source = "preguntaEvaluacion.cvePreguntaEvaluacion", target = "cvePreguntaEvaluacion")
    RespuestaEvaluacionDTO toDto (RespuestaEvaluacion entity);
}
