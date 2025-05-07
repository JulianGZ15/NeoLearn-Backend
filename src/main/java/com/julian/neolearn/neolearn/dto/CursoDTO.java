package com.julian.neolearn.neolearn.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Data;

@Data
public class CursoDTO {
    private Long cveCurso;
    private Long cveEmpresa;
    private String titulo;
    private String descripcion;
    private BigDecimal precio;
    private Boolean es_gratis;
    private String publico_objetivo;
    private LocalDate fecha_publicacion;
    private String estado;
}

