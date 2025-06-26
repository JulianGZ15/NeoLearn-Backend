package com.julian.neolearn.neolearn.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ProgramarClaseRequest {
    private String titulo;
    private String descripcion;
    private LocalDateTime fechaProgramada;
    private Integer duracionEstimadaMinutos;
    
    // Getters y setters
}
