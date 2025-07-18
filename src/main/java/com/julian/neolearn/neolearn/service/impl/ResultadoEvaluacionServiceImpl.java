package com.julian.neolearn.neolearn.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.julian.neolearn.neolearn.dto.ResultadoEvaluacionDTO;
import com.julian.neolearn.neolearn.entity.Evaluacion;
import com.julian.neolearn.neolearn.entity.ResultadoEvaluacion;
import com.julian.neolearn.neolearn.entity.Usuario;
import com.julian.neolearn.neolearn.mapper.ResultadoEvaluacionMapper;
import com.julian.neolearn.neolearn.repository.EvaluacionRepository;
import com.julian.neolearn.neolearn.repository.ResultadoEvaluacionRepository;
import com.julian.neolearn.neolearn.repository.UsuarioRepository;
import com.julian.neolearn.neolearn.service.ResultadoEvaluacionService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ResultadoEvaluacionServiceImpl implements ResultadoEvaluacionService {

    private final ResultadoEvaluacionRepository resultadoEvaluacionRepository;
    private final ResultadoEvaluacionMapper resultadoEvaluacionMapper;
    private final UsuarioRepository usuarioRepository;
    private final EvaluacionRepository evaluacionRepository;

    @Override
    @Transactional(readOnly = true)
    public Optional<ResultadoEvaluacionDTO> buscarResultadoEvaluacionPorId(Long cveResultadoEvaluacion){
        ResultadoEvaluacion resultado = resultadoEvaluacionRepository.findById(cveResultadoEvaluacion)
                .orElseThrow( () -> new RuntimeException("Resultados no encontrados"));
        
        return Optional.of(resultadoEvaluacionMapper.toDTO(resultado));
    }


    @Override
    @Transactional(readOnly = true)
    public List<ResultadoEvaluacionDTO> buscarResultadoEvaluacionPorEvaluacion(Long cveEvaluacion) {
         Evaluacion evaluacion = evaluacionRepository.findById(cveEvaluacion)
                .orElseThrow(() -> new RuntimeException("Evaluación no encontrada"));
        
        return resultadoEvaluacionRepository.findByEvaluacion(evaluacion).stream()
                .map(resultadoEvaluacionMapper::toDTO).toList();

    }

    @Override
    @Transactional
    public ResultadoEvaluacionDTO guardarResultadoEvaluacion(ResultadoEvaluacionDTO dto) {

        String correo = SecurityContextHolder.getContext().getAuthentication().getName();

        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        System.out.println("Usuario encontrado: " + usuario.getNombre());
        Evaluacion evaluacion = evaluacionRepository.findById(dto.getCveEvaluacion())
                .orElseThrow(() -> new RuntimeException("Evaluación no encontrada"));
        System.out.println("Evaluacion encontrada: " + evaluacion.getTitulo());

        ResultadoEvaluacion resultadoEvaluacion = resultadoEvaluacionMapper.toEntity(dto);
        
        resultadoEvaluacion.setEvaluacion(evaluacion);
        resultadoEvaluacion.setUsuario(usuario);
        resultadoEvaluacion.setFecha(LocalDateTime.now());

        ResultadoEvaluacion saved = resultadoEvaluacionRepository.save(resultadoEvaluacion);
        return resultadoEvaluacionMapper.toDTO(saved);
    }

    @Override
    @Transactional
    public void borrarResultadoEvaluacionPorId(Long cveResultadoEvaluacion) {

        resultadoEvaluacionRepository.deleteById(cveResultadoEvaluacion);
    }
}
