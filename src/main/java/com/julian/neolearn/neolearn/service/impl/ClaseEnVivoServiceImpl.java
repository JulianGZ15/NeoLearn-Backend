package com.julian.neolearn.neolearn.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.julian.neolearn.neolearn.dto.ClaseEnVivoDTO;
import com.julian.neolearn.neolearn.dto.ProgramarClaseRequest;
import com.julian.neolearn.neolearn.dto.SalaEnVivoDTO;
import com.julian.neolearn.neolearn.entity.*;
import com.julian.neolearn.neolearn.entity.ClaseEnVivo.EstadoClase;
import com.julian.neolearn.neolearn.mapper.ClaseEnVivoMapper;
import com.julian.neolearn.neolearn.mapper.SalaEnVivoMapper;
import com.julian.neolearn.neolearn.repository.ClaseEnVivoRepository;
import com.julian.neolearn.neolearn.repository.SalaEnVivoRepository;
import com.julian.neolearn.neolearn.repository.UsuarioRepository;
import com.julian.neolearn.neolearn.service.ClaseEnVivoService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ClaseEnVivoServiceImpl implements ClaseEnVivoService {

    private final ClaseEnVivoRepository claseRepo;
    private final SalaEnVivoRepository salaRepo;
    private final UsuarioRepository usuarioRepo;
    private final ClaseEnVivoMapper claseMapper;
    private final SalaEnVivoMapper salaMapper;

    @Override
    public ClaseEnVivoDTO programarClase(ProgramarClaseRequest request, Long idSala, Long idInstructor) {
        SalaEnVivo sala = salaRepo.findById(idSala)
            .orElseThrow(() -> new EntityNotFoundException("Sala no encontrada"));
        Usuario instructor = usuarioRepo.findById(idInstructor)
            .orElseThrow(() -> new EntityNotFoundException("Instructor no encontrado"));

        // Validar que no haya conflictos de horario
        validarConflictoHorario(idSala, request.getFechaProgramada(), request.getDuracionEstimadaMinutos());

        ClaseEnVivo clase = new ClaseEnVivo();
        clase.setTitulo(request.getTitulo());
        clase.setDescripcion(request.getDescripcion());
        clase.setFechaProgramada(request.getFechaProgramada());
        clase.setDuracionEstimadaMinutos(request.getDuracionEstimadaMinutos());
        clase.setSalaEnVivo(sala);
        clase.setInstructor(instructor);
        clase.setEstado(EstadoClase.PROGRAMADA);

        return claseMapper.toDTO(claseRepo.save(clase));
    }

    @Override
    public ClaseEnVivoDTO reprogramarClase(Long idClase, LocalDateTime nuevaFecha) {
        ClaseEnVivo clase = claseRepo.findById(idClase)
            .orElseThrow(() -> new EntityNotFoundException("Clase no encontrada"));
        
        if (clase.getEstado() == EstadoClase.EN_VIVO) {
            throw new IllegalStateException("No se puede reprogramar una clase en vivo");
        }

        validarConflictoHorario(clase.getSalaEnVivo().getId(), nuevaFecha, clase.getDuracionEstimadaMinutos());
        
        clase.setFechaProgramada(nuevaFecha);
        return claseMapper.toDTO(claseRepo.save(clase));
    }

    @Override
    public void cancelarClase(Long idClase) {
        ClaseEnVivo clase = claseRepo.findById(idClase)
            .orElseThrow(() -> new EntityNotFoundException("Clase no encontrada"));
        
        if (clase.getEstado() == EstadoClase.EN_VIVO) {
            throw new IllegalStateException("No se puede cancelar una clase en vivo");
        }

        clase.setEstado(EstadoClase.CANCELADA);
        claseRepo.save(clase);
    }

    @Override
    public ClaseEnVivoDTO iniciarTransmision(Long idClase) {
        ClaseEnVivo clase = claseRepo.findById(idClase)
            .orElseThrow(() -> new EntityNotFoundException("Clase no encontrada"));
        
        if (clase.getEstado() != EstadoClase.PROGRAMADA) {
            throw new IllegalStateException("Solo se pueden iniciar clases programadas");
        }

        // Activar la sala
        SalaEnVivo sala = clase.getSalaEnVivo();
        if (!sala.getActiva()) {
            sala.setActiva(true);
            sala.setFechaInicio(LocalDateTime.now());
            salaRepo.save(sala);
        }

        // Iniciar la clase
        clase.setEstado(EstadoClase.EN_VIVO);
        clase.setFechaInicio(LocalDateTime.now());
        
        return claseMapper.toDTO(claseRepo.save(clase));
    }

    @Override
    public ClaseEnVivoDTO finalizarTransmision(Long idClase) {
        ClaseEnVivo clase = claseRepo.findById(idClase)
            .orElseThrow(() -> new EntityNotFoundException("Clase no encontrada"));
        
        if (clase.getEstado() != EstadoClase.EN_VIVO) {
            throw new IllegalStateException("Solo se pueden finalizar clases en vivo");
        }

        clase.setEstado(EstadoClase.FINALIZADA);
        clase.setFechaFin(LocalDateTime.now());
        clase.setFinalizada(true);

        // Verificar si hay más clases activas en la sala
        SalaEnVivo sala = clase.getSalaEnVivo();
        boolean hayOtrasClasesActivas = claseRepo.findClasesEnVivo().stream()
            .anyMatch(c -> c.getSalaEnVivo().getId().equals(sala.getId()) && 
                          !c.getCveClaseEnVivo().equals(idClase));

        if (!hayOtrasClasesActivas) {
            sala.setActiva(false);
            sala.setFechaFin(LocalDateTime.now());
            salaRepo.save(sala);
        }

        return claseMapper.toDTO(claseRepo.save(clase));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClaseEnVivoDTO> listarPorCurso(Long idCurso) {
        return claseRepo.findBySalaEnVivo_Curso_cveCurso(idCurso)
                .stream()
                .map(claseMapper::toDTO)
                .sorted((c1, c2) -> c1.getFechaProgramada().compareTo(c2.getFechaProgramada()))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClaseEnVivoDTO> listarPorInstructor(Long instructorId) {
        return claseRepo.findClasesPorInstructorYEstado(instructorId, EstadoClase.PROGRAMADA)
                .stream()
                .map(claseMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClaseEnVivoDTO> listarClasesEnVivo() {
        return claseRepo.findClasesEnVivo()
                .stream()
                .map(claseMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClaseEnVivoDTO> listarClasesProgramadas(Long cursoId, LocalDate fecha) {
        LocalDateTime inicio = fecha.atStartOfDay();
        LocalDateTime fin = fecha.atTime(23, 59, 59);
        
        return claseRepo.findClasesPorCursoYRangoFecha(cursoId, inicio, fin)
                .stream()
                .map(claseMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ClaseEnVivoDTO> obtenerDetalle(Long idClase) {
        return claseRepo.findById(idClase).map(claseMapper::toDTO);
    }

    @Override
    public SalaEnVivoDTO activarSala(Long idSala) {
        SalaEnVivo sala = salaRepo.findById(idSala)
            .orElseThrow(() -> new EntityNotFoundException("Sala no encontrada"));
        
        sala.setActiva(true);
        sala.setFechaInicio(LocalDateTime.now());
        return salaMapper.toDTO(salaRepo.save(sala));
    }

    @Override
    public SalaEnVivoDTO finalizarSala(Long idSala) {
        SalaEnVivo sala = salaRepo.findById(idSala)
            .orElseThrow(() -> new EntityNotFoundException("Sala no encontrada"));
        
        // Finalizar todas las clases activas
        List<ClaseEnVivo> clasesActivas = claseRepo.findClasesEnVivo().stream()
            .filter(c -> c.getSalaEnVivo().getId().equals(idSala))
            .toList();
        
        for (ClaseEnVivo clase : clasesActivas) {
            clase.setEstado(EstadoClase.FINALIZADA);
            clase.setFechaFin(LocalDateTime.now());
            clase.setFinalizada(true);
        }
        
        sala.setActiva(false);
        sala.setFechaFin(LocalDateTime.now());
        return salaMapper.toDTO(salaRepo.save(sala));
    }

    @Override
    public void agregarParticipante(Long idSala, Long idUsuario) {
        SalaEnVivo sala = salaRepo.findById(idSala)
            .orElseThrow(() -> new EntityNotFoundException("Sala no encontrada"));
        
        if (!sala.getParticipantes().contains(idUsuario)) {
            sala.getParticipantes().add(idUsuario);
            salaRepo.save(sala);
        }
    }

    @Override
    public void removerParticipante(Long idSala, Long idUsuario) {
        SalaEnVivo sala = salaRepo.findById(idSala)
            .orElseThrow(() -> new EntityNotFoundException("Sala no encontrada"));
        
        sala.getParticipantes().remove(idUsuario);
        salaRepo.save(sala);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Long> obtenerParticipantes(Long idSala) {
        return salaRepo.findById(idSala)
            .map(SalaEnVivo::getParticipantes)
            .orElse(new ArrayList<>());
    }

    private void validarConflictoHorario(Long idSala, LocalDateTime fechaProgramada, Integer duracionMinutos) {
        LocalDateTime finClase = fechaProgramada.plusMinutes(duracionMinutos != null ? duracionMinutos : 60);
        
        List<ClaseEnVivo> clasesConflicto = claseRepo.findClasesProgramadasEnRango(
            fechaProgramada.minusMinutes(60), finClase.plusMinutes(60)
        ).stream()
        .filter(c -> c.getSalaEnVivo().getId().equals(idSala))
        .filter(c -> c.getEstado() != EstadoClase.CANCELADA)
        .toList();
        
        if (!clasesConflicto.isEmpty()) {
            throw new IllegalArgumentException("Ya existe una clase programada en ese horario para esta sala");
        }
    }
}

