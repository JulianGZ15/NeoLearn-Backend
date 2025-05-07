package com.julian.neolearn.neolearn.dto;

import java.time.LocalDate;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class ReporteCursoDTO {
    private Long cve_reporteCurso;
    private Long cve_curso;
    private LocalDate fecha;
    private Integer vistas;
    private Integer inscritos;
    private BigDecimal ingresos_generados;
}
