package com.julian.neolearn.neolearn.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.julian.neolearn.neolearn.dto.ClaseEnVivoDTO;
import com.julian.neolearn.neolearn.entity.ClaseEnVivo;
import com.julian.neolearn.neolearn.mapper.ClaseEnVivoMapper;
import com.julian.neolearn.neolearn.repository.ClaseEnVivoRepository;
import com.julian.neolearn.neolearn.service.ClaseEnVivoService;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ClaseEnVivoServiceImpl implements ClaseEnVivoService{

    @Autowired
    private ClaseEnVivoRepository claseEnVivoRepository;

    @Autowired
    private ClaseEnVivoMapper claseEnVivoMapper;


        @Override
    public Optional<ClaseEnVivoDTO> buscarClaseEnVivoPorId(Long cveClaseEnVivo) {
        return claseEnVivoRepository.findById(cveClaseEnVivo).map(claseEnVivoMapper::toDTO);
    }

    @Override
    public List<ClaseEnVivoDTO> listarClasesEnVivo() {
        return claseEnVivoRepository.findAll().stream().map(claseEnVivoMapper::toDTO).toList();
    }

    public ClaseEnVivoDTO guardarClaseEnVivo(ClaseEnVivoDTO dto) {
        ClaseEnVivo claseEnVivo = claseEnVivoMapper.toEntity(dto);

        if (dto.getCve_claseEnVivo() != null) {
            // Validar existencia previa
            Optional<ClaseEnVivo> existente = claseEnVivoRepository.findById(dto.getCve_claseEnVivo());
            if (existente.isEmpty()) {
                throw new EntityNotFoundException("ClaseEnVivo no encontrada con ID: " + dto.getCve_claseEnVivo());
            }
        }

        claseEnVivo = claseEnVivoRepository.save(claseEnVivo);
        return claseEnVivoMapper.toDTO(claseEnVivo);
    }

    @Override
    public void borrarClaseEnVivoPorId(Long cveClaseEnVivo) {

        claseEnVivoRepository.deleteById(cveClaseEnVivo);
    }

}
