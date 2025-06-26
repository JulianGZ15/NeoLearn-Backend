package com.julian.neolearn.neolearn.dto;

import java.time.LocalDateTime;

import com.julian.neolearn.neolearn.entity.ClaseEnVivo.EstadoClase;

import lombok.*;

@Data
@Builder
public class ClaseEnVivoDTO {
    
    private Long cveClaseEnVivo;
    private String titulo;
    private String descripcion;
    private LocalDateTime fechaProgramada;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private Long salaId;
    private String codigoSala;
    private Long instructorId;
    private String instructorNombre;
    private EstadoClase estado;
    private Boolean finalizada;
    private Integer duracionEstimadaMinutos;
    private Long cursoId;
    private String cursoNombre;
 }