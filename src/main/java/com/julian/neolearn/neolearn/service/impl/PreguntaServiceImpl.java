package com.julian.neolearn.neolearn.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.julian.neolearn.neolearn.dto.PreguntaDTO;
import com.julian.neolearn.neolearn.entity.Curso;
import com.julian.neolearn.neolearn.entity.Pregunta;
import com.julian.neolearn.neolearn.entity.Usuario;
import com.julian.neolearn.neolearn.mapper.PreguntaMapper;
import com.julian.neolearn.neolearn.repository.CursoRepository;
import com.julian.neolearn.neolearn.repository.PreguntaRepository;
import com.julian.neolearn.neolearn.repository.UsuarioRepository;
import com.julian.neolearn.neolearn.service.PreguntaService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PreguntaServiceImpl implements PreguntaService{

    private final PreguntaRepository preguntaRepository;
    private final PreguntaMapper preguntaMapper;
    private final UsuarioRepository usuarioRepository;
    private final CursoRepository cursoRepository;


    @Override
    @Transactional(readOnly = true)
    public Optional<PreguntaDTO> buscarPreguntaPorId(Long cvePregunta) {
        return preguntaRepository.findById(cvePregunta).map(preguntaMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PreguntaDTO> listarPreguntasPorCurso(Long cveCurso) {
        return preguntaRepository.findByCurso_CveCurso(cveCurso).stream()
                .map(preguntaMapper::toDTO)
                .toList();
         }

    @Override
    @Transactional
    public PreguntaDTO guardarPregunta(PreguntaDTO dto, Long cveCurso) {
        if (dto == null || dto.getCvePregunta() == null) {
            throw new IllegalArgumentException("La pregunta no puede ser nula");
        }
        String correo = SecurityContextHolder.getContext().getAuthentication().getName();

        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Curso curso = cursoRepository.findById(cveCurso)
                .orElseThrow(() -> new RuntimeException("Curso no encontrado"));
        
        Pregunta pregunta = preguntaMapper.toEntity(dto);
        pregunta.setCurso(curso);
        pregunta.setUsuario(usuario); 
        pregunta.setFecha(LocalDateTime.now()); 

        Pregunta saved = preguntaRepository.save(pregunta);
        return preguntaMapper.toDTO(saved);

    }

    @Override
    @Transactional
    public void borrarPreguntaPorId(Long cvePregunta) {

        preguntaRepository.deleteById(cvePregunta);
    }


}
