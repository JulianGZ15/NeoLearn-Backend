package com.julian.neolearn.neolearn.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.julian.neolearn.neolearn.dto.ReporteCursoDTO;
import com.julian.neolearn.neolearn.entity.ReporteCurso;
import com.julian.neolearn.neolearn.mapper.ReporteCursoMapper;
import com.julian.neolearn.neolearn.repository.ReporteCursoRepository;
import com.julian.neolearn.neolearn.service.ReporteCursoService;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ReporteCusroServiceImpl implements ReporteCursoService {

    @Autowired
    private ReporteCursoRepository reporteCursoRepository;
    @Autowired
    private ReporteCursoMapper reporteCursoMapper;



        @Override
    public Optional<ReporteCursoDTO> buscarReporteCursoPorId(Long cveReporteCurso) {
        return reporteCursoRepository.findById(cveReporteCurso).map(reporteCursoMapper::toDTO);
    }

    @Override
    public List<ReporteCursoDTO> listarReportesCurso() {
        return reporteCursoRepository.findAll().stream().map(reporteCursoMapper::toDTO).toList();
    }

    public ReporteCursoDTO guardarReporteCurso(ReporteCursoDTO dto) {
        ReporteCurso reporteCurso = reporteCursoMapper.toEntity(dto);

        if (dto.getCve_reporteCurso() != null) {
            // Validar existencia previa
            Optional<ReporteCurso> existente = reporteCursoRepository.findById(dto.getCve_reporteCurso());
            if (existente.isEmpty()) {
                throw new EntityNotFoundException("ReporteCurso no encontrada con ID: " + dto.getCve_reporteCurso());
            }
        }

        reporteCurso = reporteCursoRepository.save(reporteCurso);
        return reporteCursoMapper.toDTO(reporteCurso);
    }

    @Override
    public void borrarReporteCursoPorId(Long cveReporteCurso) {

        reporteCursoRepository.deleteById(cveReporteCurso);
    }

}
