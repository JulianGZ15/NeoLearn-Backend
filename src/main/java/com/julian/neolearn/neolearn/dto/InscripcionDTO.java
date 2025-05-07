package com.julian.neolearn.neolearn.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Data;

@Data
public class InscripcionDTO {
    private Long cveInscripcion;
    private Long cveUsuario;
    private Long cveCurso;
    private LocalDate fecha_inscripcion;
    private BigDecimal precio_pagado;
    private String metodo_pago;
    private String estado;
}

