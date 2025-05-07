package com.julian.neolearn.neolearn.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class CuponDTO {
    private Long cve_cupon;
    private Long cve_curso;
    private String codigo;
    private BigDecimal descuento_porcentaje;
    private BigDecimal descuento_fijo;
    private LocalDate fecha_inicio;
    private LocalDate fecha_fin;
    private Integer usos_disponibles;
}

