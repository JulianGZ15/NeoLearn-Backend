package com.julian.neolearn.neolearn.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.julian.neolearn.neolearn.dto.PreguntaEvaluacionDTO;
import com.julian.neolearn.neolearn.entity.PreguntaEvaluacion;
import com.julian.neolearn.neolearn.mapper.PreguntaEvaluacionMapper;
import com.julian.neolearn.neolearn.repository.EvaluacionRepository;
import com.julian.neolearn.neolearn.repository.PreguntaEvaluacionRepository;
import com.julian.neolearn.neolearn.service.PreguntaEvaluacionService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PreguntaEvaluacionServiceImpl implements PreguntaEvaluacionService {

    private final PreguntaEvaluacionRepository preguntaEvaluacionRepository;
    private final PreguntaEvaluacionMapper preguntaEvaluacionMapper;
    private final EvaluacionRepository evaluacionRepository;



    @Override
    @Transactional(readOnly = true)
    public Optional<PreguntaEvaluacionDTO> buscarPreguntaEvaluacionPorId(Long cvePreguntaEvaluacion) {
        return preguntaEvaluacionRepository.findById(cvePreguntaEvaluacion).map(preguntaEvaluacionMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PreguntaEvaluacionDTO> listarPreguntasPorEvaluacion(Long cveEvaluacion) {
        return preguntaEvaluacionRepository.findByEvaluacion_CveEvaluacion(cveEvaluacion).stream()
                .map(preguntaEvaluacionMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional
    public PreguntaEvaluacionDTO guardarPreguntaEvaluacion(PreguntaEvaluacionDTO dto, Long cveEvaluacion) {
        if (dto == null ) {
            throw new IllegalArgumentException("La pregunta de evaluación no puede ser nula o no tener ID");
        }

        PreguntaEvaluacion preguntaEvaluacion = preguntaEvaluacionMapper.toEntity(dto);
        preguntaEvaluacion.setEvaluacion(evaluacionRepository.findById(cveEvaluacion)
                .orElseThrow(() -> new EntityNotFoundException("Evaluación no encontrada")));


        preguntaEvaluacion = preguntaEvaluacionRepository.save(preguntaEvaluacion);
        return preguntaEvaluacionMapper.toDTO(preguntaEvaluacion);
    }

    @Override
    @Transactional
    public PreguntaEvaluacionDTO actualizarPregunta(PreguntaEvaluacionDTO preguntaEvaluacion) {
        if (preguntaEvaluacion == null || preguntaEvaluacion.getCvePreguntaEvaluacion() == null) {
            throw new IllegalArgumentException("La pregunta de evaluación no puede ser nula o no tener ID");
        }

        PreguntaEvaluacion pregunta = preguntaEvaluacionMapper.toEntity(preguntaEvaluacion);
        pregunta = preguntaEvaluacionRepository.save(pregunta);
        return preguntaEvaluacionMapper.toDTO(pregunta);
    }

    @Override
    public void borrarPreguntaEvaluacionPorId(Long cvePreguntaEvaluacion) {

        preguntaEvaluacionRepository.deleteById(cvePreguntaEvaluacion);
    }

}
