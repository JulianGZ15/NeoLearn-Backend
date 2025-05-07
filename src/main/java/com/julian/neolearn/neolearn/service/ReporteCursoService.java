package com.julian.neolearn.neolearn.service;


import java.util.List;
import java.util.Optional;

import com.julian.neolearn.neolearn.dto.ReporteCursoDTO;
public interface ReporteCursoService {

    Optional<ReporteCursoDTO> buscarReporteCursoPorId(Long cveReporteCurso);
    List<ReporteCursoDTO> listarReportesCurso();
    ReporteCursoDTO guardarReporteCurso(ReporteCursoDTO reporteCurso);
    void borrarReporteCursoPorId(Long cveReporteCurso);
    

}
