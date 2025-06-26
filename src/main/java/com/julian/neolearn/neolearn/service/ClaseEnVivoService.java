package com.julian.neolearn.neolearn.service;

import java.time.*;
import java.util.List;
import java.util.Optional;

import com.julian.neolearn.neolearn.dto.ClaseEnVivoDTO;
import com.julian.neolearn.neolearn.dto.ProgramarClaseRequest;
import com.julian.neolearn.neolearn.dto.SalaEnVivoDTO;

public interface ClaseEnVivoService {
    
    // Programación de clases
    ClaseEnVivoDTO programarClase(ProgramarClaseRequest request, Long idSala, Long idInstructor);
    ClaseEnVivoDTO reprogramarClase(Long idClase, LocalDateTime nuevaFecha);
    void cancelarClase(Long idClase);
    
    // Gestión de transmisión
    ClaseEnVivoDTO iniciarTransmision(Long idClase);
    ClaseEnVivoDTO finalizarTransmision(Long idClase);
    
    // Consultas
    List<ClaseEnVivoDTO> listarPorCurso(Long idCurso);
    List<ClaseEnVivoDTO> listarPorInstructor(Long instructorId);
    List<ClaseEnVivoDTO> listarClasesEnVivo();
    List<ClaseEnVivoDTO> listarClasesProgramadas(Long cursoId, LocalDate fecha);
    Optional<ClaseEnVivoDTO> obtenerDetalle(Long idClase);
    
    // Gestión de salas
    SalaEnVivoDTO activarSala(Long idSala);
    SalaEnVivoDTO finalizarSala(Long idSala);
    
    // Participantes
    void agregarParticipante(Long idSala, Long idUsuario);
    void removerParticipante(Long idSala, Long idUsuario);
    List<Long> obtenerParticipantes(Long idSala);
}
