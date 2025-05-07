package com.julian.neolearn.neolearn.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Data;

@Data
public class ComisionDTO {
    private Long cve_comision;
    private Long cve_curso;
    private BigDecimal porcentaje;
    private BigDecimal monto;
    private LocalDate fecha_generacion;
}
