package com.julian.neolearn.neolearn.service.impl;

import java.util.Optional;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.julian.neolearn.neolearn.dto.RespuestaDTO;
import com.julian.neolearn.neolearn.entity.Pregunta;
import com.julian.neolearn.neolearn.entity.Respuesta;
import com.julian.neolearn.neolearn.entity.Usuario;
import com.julian.neolearn.neolearn.mapper.RespuestaMapper;
import com.julian.neolearn.neolearn.repository.PreguntaRepository;
import com.julian.neolearn.neolearn.repository.RespuestaRepository;
import com.julian.neolearn.neolearn.repository.UsuarioRepository;
import com.julian.neolearn.neolearn.service.RespuestaService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RespuestaServiceImpl implements RespuestaService {

    private final RespuestaRepository respuestaRepository;
    private final RespuestaMapper respuestaMapper;
    private final UsuarioRepository usuarioRepository;
    private final PreguntaRepository preguntaRepository;



    @Override
    @Transactional(readOnly = true)
    public Optional<RespuestaDTO> buscarRespuestaPorId(Long cveRespuesta) {
        return respuestaRepository.findById(cveRespuesta)
                .map(respuestaMapper::toDTO);
    }


    @Override
    @Transactional
    public RespuestaDTO guardarRespuesta(RespuestaDTO dto, Long cvePregunta) {
        if (dto == null || cvePregunta == null) {
            throw new IllegalArgumentException("Los datos de la respuesta y la pregunta son obligatorios");
        }

        String correo = SecurityContextHolder.getContext().getAuthentication().getName();

        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Respuesta respuesta = respuestaMapper.toEntity(dto);
        Pregunta pregunta = preguntaRepository.findById(cvePregunta)
                .orElseThrow(() -> new EntityNotFoundException("Pregunta no encontrada"));

        
        respuesta.setPregunta(pregunta);
        respuesta.setUsuario(usuario);
        respuesta = respuestaRepository.save(respuesta);
        return respuestaMapper.toDTO(respuesta);
    }

    @Override
    @Transactional
    public void borrarRespuestaPorId(Long cveRespuesta) {

        respuestaRepository.deleteById(cveRespuesta);
    }
}
