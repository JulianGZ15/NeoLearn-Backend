package com.julian.neolearn.neolearn.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.julian.neolearn.neolearn.dto.InscripcionDTO;
import com.julian.neolearn.neolearn.entity.Curso;
import com.julian.neolearn.neolearn.entity.Inscripcion;
import com.julian.neolearn.neolearn.entity.Usuario;
import com.julian.neolearn.neolearn.entity.Inscripcion.EstadoInscripcion;
import com.julian.neolearn.neolearn.entity.Usuario.TipoUsuario;
import com.julian.neolearn.neolearn.mapper.InscripcionMapper;
import com.julian.neolearn.neolearn.repository.CursoRepository;
import com.julian.neolearn.neolearn.repository.InscripcionRepository;
import com.julian.neolearn.neolearn.repository.UsuarioRepository;
import com.julian.neolearn.neolearn.service.InscripcionService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InscripcionServiceImpl implements InscripcionService {

    private final InscripcionRepository inscripcionRepository;
    private final InscripcionMapper inscripcionMapper;
    private final UsuarioRepository usuarioRepository;
    private final CursoRepository cursoRepository;


    @Transactional(readOnly = true)
    @Override
    public Optional<InscripcionDTO> buscarInscripcionPorId(Long cveInscripcion) {
        String correo = SecurityContextHolder.getContext().getAuthentication().getName();

        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!usuario.getTipo().equals(TipoUsuario.EMPRESARIAL)) {
            throw new RuntimeException("No tienes permiso para ver las inscripciones de este curso");
        }

        return inscripcionRepository.findById(cveInscripcion)
             .map(inscripcionMapper::toDTO);
    }

    @Transactional(readOnly = true)
    @Override
    public List<InscripcionDTO> listarInscripcionesPorCurso(Long cveCurso) {
        String correo = SecurityContextHolder.getContext().getAuthentication().getName();

        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!usuario.getTipo().equals(TipoUsuario.EMPRESARIAL)) {
            throw new RuntimeException("No tienes permiso para ver las inscripciones de este curso");
        }
        Curso curso = cursoRepository.findById(cveCurso)
                .orElseThrow(() -> new RuntimeException("Curso no encontrado con ID: " + cveCurso));
        return inscripcionRepository.findByCurso(curso).stream()
        .map(inscripcionMapper::toDTO)
        .toList();
     }


    @Transactional
    @Override
    public InscripcionDTO guardarInscripcion(InscripcionDTO dto, Long cveCurso) {
        if (dto == null || cveCurso == null) {
            throw new IllegalArgumentException("La inscripción y el ID del curso no pueden ser nulos");
        }

        String correo = SecurityContextHolder.getContext().getAuthentication().getName();

        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Inscripcion inscripcion = inscripcionMapper.toEntity(dto);

        inscripcion.setUsuario(usuario);
        inscripcion.setCurso(cursoRepository.findById(cveCurso) 
                .orElseThrow(() -> new RuntimeException("Curso no encontrado con ID: " + cveCurso)));
        inscripcion.setFecha_inscripcion(LocalDate.now());
        inscripcion.setEstado(EstadoInscripcion.ACTIVA);
        inscripcion.setPrecio_pagado(inscripcion.getCurso().getPrecio());
        Inscripcion saved = inscripcionRepository.save(inscripcion);
        return inscripcionMapper.toDTO(saved);
    }

    @Transactional
    @Override
    public void borrarInscripcionPorId(Long cveInscripcion) {
        Inscripcion inscripcion = inscripcionRepository.findById(cveInscripcion)
                .orElseThrow(() -> new RuntimeException("Inscripción no encontrada con ID: " + cveInscripcion));
        inscripcionRepository.delete(inscripcion);
    }

}
