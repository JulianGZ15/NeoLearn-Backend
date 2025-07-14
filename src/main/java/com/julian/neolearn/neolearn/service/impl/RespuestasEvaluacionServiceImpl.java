package com.julian.neolearn.neolearn.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.julian.neolearn.neolearn.dto.RespuestaEvaluacionDTO;
import com.julian.neolearn.neolearn.entity.RespuestaEvaluacion;
import com.julian.neolearn.neolearn.mapper.RespuestaEvaluacionMapper;
import com.julian.neolearn.neolearn.repository.RespuestasEvaluacionRepository;
import com.julian.neolearn.neolearn.service.RespuestasEvaluacionService;

public class RespuestasEvaluacionServiceImpl implements RespuestasEvaluacionService{

    @Autowired
     private RespuestasEvaluacionRepository respuestasEvaluacionRepository;
     @Autowired
     private RespuestaEvaluacionMapper respuestaEvaluacionMapper;



    @Override
    public RespuestaEvaluacionDTO guardarRespuestaEvaluacion (RespuestaEvaluacionDTO respuestaEvaluacion){

        RespuestaEvaluacion respuesta = respuestaEvaluacionMapper.toEntity(respuestaEvaluacion);
        RespuestaEvaluacion respuestaGuardada = respuestasEvaluacionRepository.save(respuesta);
        return respuestaEvaluacionMapper.toDto(respuestaGuardada);
    }
    
    @Override
    public List<RespuestaEvaluacionDTO> guardarRespuestasEvaluacion (List<RespuestaEvaluacionDTO> respuestasEvaluacion){
        respuestasEvaluacionRepository.saveAll(
            respuestasEvaluacion.stream()
                .map(respuestaEvaluacionMapper::toEntity)
                .toList()
        );

        return respuestasEvaluacion.stream()
                .toList();

    }

    @Override
    public List<RespuestaEvaluacionDTO> buscarRespuestasPorResultadoEvaluacion(Long cveResultadoEvaluacion){
        return respuestasEvaluacionRepository.findByResultadoEvaluacion_CveResultadoEvaluacion(cveResultadoEvaluacion).stream()
                .map(respuestaEvaluacionMapper::toDto)
                .toList();
    }
    
    @Override
    public void borrarRespuestasEvaluacionPoResultadoEvaluacion(Long cveResultadoEvaluacion){
        List<RespuestaEvaluacion> respuestas = respuestasEvaluacionRepository.findByResultadoEvaluacion_CveResultadoEvaluacion(cveResultadoEvaluacion);
        if (!respuestas.isEmpty()){
            respuestasEvaluacionRepository.deleteAll(respuestas);
        }
    }


}
