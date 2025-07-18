package com.julian.neolearn.neolearn.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.julian.neolearn.neolearn.dto.RespuestaEvaluacionDTO;
import com.julian.neolearn.neolearn.entity.PreguntaEvaluacion;
import com.julian.neolearn.neolearn.entity.RespuestaEvaluacion;
import com.julian.neolearn.neolearn.entity.ResultadoEvaluacion;
import com.julian.neolearn.neolearn.mapper.RespuestaEvaluacionMapper;
import com.julian.neolearn.neolearn.repository.PreguntaEvaluacionRepository;
import com.julian.neolearn.neolearn.repository.RespuestasEvaluacionRepository;
import com.julian.neolearn.neolearn.repository.ResultadoEvaluacionRepository;
import com.julian.neolearn.neolearn.service.RespuestasEvaluacionService;

import jakarta.transaction.Transactional;

@Service
public class RespuestasEvaluacionServiceImpl implements RespuestasEvaluacionService {

    @Autowired
    private RespuestasEvaluacionRepository respuestasEvaluacionRepository;
    @Autowired
    private RespuestaEvaluacionMapper respuestaEvaluacionMapper;
    @Autowired
    private ResultadoEvaluacionRepository resultadoEvaluacionRepository;
    @Autowired
    private PreguntaEvaluacionRepository preguntaEvaluacionRepository;

@Override
@Transactional
public RespuestaEvaluacionDTO guardarRespuestaEvaluacion(RespuestaEvaluacionDTO dto) {
    ResultadoEvaluacion resultado = resultadoEvaluacionRepository.findById(dto.getCveResultadoEvaluacion())
        .orElseThrow(() -> new RuntimeException("ResultadoEvaluacion no encontrado"));

    PreguntaEvaluacion pregunta = preguntaEvaluacionRepository.findById(dto.getCvePreguntaEvaluacion())
        .orElseThrow(() -> new RuntimeException("PreguntaEvaluacion no encontrada"));

    RespuestaEvaluacion respuesta = RespuestaEvaluacion.builder()
        .cveRespuestaEvaluacion(dto.getCveRespuestaEvaluacion())
        .respuestaUsuario(dto.getRespuestaUsuario())
        .resultadoEvaluacion(resultado)
        .preguntaEvaluacion(pregunta)
        .build();

    return respuestaEvaluacionMapper.toDto(respuestasEvaluacionRepository.save(respuesta));
    }

@Override
@Transactional
public List<RespuestaEvaluacionDTO> guardarRespuestasEvaluacion(List<RespuestaEvaluacionDTO> dtos) {
    List<RespuestaEvaluacion> respuestas = dtos.stream().map(dto -> {
        ResultadoEvaluacion resultado = resultadoEvaluacionRepository.findById(dto.getCveResultadoEvaluacion())
            .orElseThrow(() -> new RuntimeException("ResultadoEvaluacion no encontrado"));
        PreguntaEvaluacion pregunta = preguntaEvaluacionRepository.findById(dto.getCvePreguntaEvaluacion())
            .orElseThrow(() -> new RuntimeException("PreguntaEvaluacion no encontrada"));

        return RespuestaEvaluacion.builder()
            .cveRespuestaEvaluacion(dto.getCveRespuestaEvaluacion())
            .respuestaUsuario(dto.getRespuestaUsuario())
            .resultadoEvaluacion(resultado)
            .preguntaEvaluacion(pregunta)
            .build();
    }).toList();

    List<RespuestaEvaluacion> guardadas = respuestasEvaluacionRepository.saveAll(respuestas);
    return guardadas.stream().map(respuestaEvaluacionMapper::toDto).toList();
}


    @Override
    public List<RespuestaEvaluacionDTO> buscarRespuestasPorResultadoEvaluacion(Long cveResultadoEvaluacion) {
        return respuestasEvaluacionRepository.findByResultadoEvaluacion_CveResultadoEvaluacion(cveResultadoEvaluacion)
                .stream()
                .map(respuestaEvaluacionMapper::toDto)
                .toList();
    }

    @Override
    public void borrarRespuestasEvaluacionPoResultadoEvaluacion(Long cveResultadoEvaluacion) {
        List<RespuestaEvaluacion> respuestas = respuestasEvaluacionRepository
                .findByResultadoEvaluacion_CveResultadoEvaluacion(cveResultadoEvaluacion);
        if (!respuestas.isEmpty()) {
            respuestasEvaluacionRepository.deleteAll(respuestas);
        }
    }

}
