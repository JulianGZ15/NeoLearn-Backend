package com.julian.neolearn.neolearn.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.julian.neolearn.neolearn.dto.EvaluacionDTO;
import com.julian.neolearn.neolearn.entity.Curso;
import com.julian.neolearn.neolearn.entity.Evaluacion;
import com.julian.neolearn.neolearn.mapper.EvaluacionMapper;
import com.julian.neolearn.neolearn.repository.CursoRepository;
import com.julian.neolearn.neolearn.repository.EvaluacionRepository;
import com.julian.neolearn.neolearn.service.EvaluacionService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EvaluacionServiceImpl implements EvaluacionService {

    private final EvaluacionRepository evaluacionRepository;
    private final EvaluacionMapper evaluacionMapper;
    private final CursoRepository cursoRepository;



    @Override
    public Optional<EvaluacionDTO> buscarEvaluacionPorId(Long cveEvaluacion) {
        return evaluacionRepository.findById(cveEvaluacion).map(evaluacionMapper::toDTO);
    }

    @Override
    public List<EvaluacionDTO> listarEvaluacionesPorCurso(Long cveCurso) {
        return evaluacionRepository.findByCurso_CveCurso(cveCurso).stream()
                .map(evaluacionMapper::toDTO)
                .toList();
    }


    @Override
    @Transactional
    public EvaluacionDTO guardarEvaluacion(EvaluacionDTO dto, Long cveCurso) {
        if (dto == null ) {
            throw new IllegalArgumentException("La evaluación no puede ser nula o no tener ID");
        }
        Curso curso = cursoRepository.findById(cveCurso)
                .orElseThrow(() -> new RuntimeException("Curso no encontrado"));
        Evaluacion evaluacion = evaluacionMapper.toEntity(dto);

        evaluacion.setCurso(curso);
        evaluacion = evaluacionRepository.save(evaluacion);
        return evaluacionMapper.toDTO(evaluacion);
    }

    @Override
    public void borrarEvaluacionPorId(Long cveEvaluacion) {

        evaluacionRepository.deleteById(cveEvaluacion);
    }
}
