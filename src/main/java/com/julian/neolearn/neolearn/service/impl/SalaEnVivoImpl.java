package com.julian.neolearn.neolearn.service.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.julian.neolearn.neolearn.dto.SalaEnVivoDTO;
import com.julian.neolearn.neolearn.entity.Curso;
import com.julian.neolearn.neolearn.entity.SalaEnVivo;
import com.julian.neolearn.neolearn.mapper.SalaEnVivoMapper;
import com.julian.neolearn.neolearn.repository.CursoRepository;
import com.julian.neolearn.neolearn.repository.SalaEnVivoRepository;
import com.julian.neolearn.neolearn.service.SalaEnVivoService;

import lombok.RequiredArgsConstructor;

@Service    
@RequiredArgsConstructor
public class SalaEnVivoImpl implements SalaEnVivoService {

        private final SalaEnVivoRepository salaRepo;
    private final CursoRepository cursoRepo;
    private final SalaEnVivoMapper salaMapper;

    @Override
    public SalaEnVivoDTO crearSala(Long idCurso) {
        Curso curso = cursoRepo.findById(idCurso).orElseThrow();

        SalaEnVivo sala = new SalaEnVivo();
        sala.setCurso(curso);
        sala.setActiva(false);
        sala.setCodigoSala(UUID.randomUUID().toString());

        return salaMapper.toDTO(salaRepo.save(sala));
    }

    @Override
    public SalaEnVivoDTO obtenerPorId(Long idSala) {
        return salaRepo.findById(idSala)
                .map(salaMapper::toDTO)
                .orElseThrow();
    }

    @Override
    public List<SalaEnVivoDTO> obtenerPorCurso(Long idCurso) {
        return salaRepo.findAll().stream()
                .filter(s -> s.getCurso().getCveCurso().equals(idCurso))
                .map(salaMapper::toDTO)
                .toList();
    }

    @Override
    public void eliminarSala(Long idSala) {
        salaRepo.deleteById(idSala);
    }
}
