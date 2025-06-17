package com.julian.neolearn.neolearn.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.julian.neolearn.neolearn.dto.CuponDTO;
import com.julian.neolearn.neolearn.entity.Cupon;
import com.julian.neolearn.neolearn.entity.Curso;
import com.julian.neolearn.neolearn.mapper.CuponMapper;
import com.julian.neolearn.neolearn.repository.CuponRepository;
import com.julian.neolearn.neolearn.repository.CursoRepository;
import com.julian.neolearn.neolearn.service.CuponService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CuponServiceImpl implements CuponService {

    private final CuponRepository cuponRepository;
    private final CursoRepository cursoRepository;
    private final CuponMapper cuponMapper;

    @Transactional(readOnly = true)
    @Override
    public Optional<CuponDTO> buscarCuponPorId(Long cveCupon) {
        return cuponRepository.findById(cveCupon)
                .map(cuponMapper::toDTO);
    }

    @Transactional(readOnly = true)
    @Override
    public List<CuponDTO> listarCupones(Long cveCurso) {
        return cuponRepository.findByCurso_CveCurso(cveCurso).stream()
                .map(cuponMapper::toDTO)
                .toList();
    }

    @Transactional
    @Override
    public CuponDTO guardarCupon(CuponDTO dto, Long cveCurso) {
        if (dto == null) {
            throw new IllegalArgumentException("El DTO no puede ser nulo");
        }

        Curso curso = cursoRepository.findById(cveCurso)
                .orElseThrow(() -> new IllegalArgumentException("Curso no encontrado con ID: " + dto.getCveCurso()));

        Cupon  cupon = cuponMapper.toEntity(dto);
        cupon.setCurso(curso);

        if (dto.getCveCupon() != null) {
            // Validar existencia previa
            Optional<Cupon> existente = cuponRepository.findById(dto.getCveCupon());
            if (existente.isEmpty()) {
                throw new IllegalArgumentException("Cupon no encontrado con ID: " + dto.getCveCupon());
            }
        }
        cupon = cuponRepository.save(cupon);
        return cuponMapper.toDTO(cupon);
    }

    @Transactional
    @Override
    public void borrarCuponPorId(Long cveCupon) {
        if (cveCupon == null) {
            throw new IllegalArgumentException("El ID del cupon no puede ser nulo");
        }
        cuponRepository.deleteById(cveCupon);
    }

}
 