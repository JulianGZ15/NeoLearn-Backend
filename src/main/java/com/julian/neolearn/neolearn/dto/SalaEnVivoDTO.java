package com.julian.neolearn.neolearn.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.*;

@Data
@Builder
public class SalaEnVivoDTO {
  private Long id;
    private String codigoSala;
    private Long cursoId;
    private String cursoNombre;
    private Boolean activa;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private LocalDateTime fechaCreacion;
    private List<ClaseEnVivoDTO> clases;
    private List<Long> participantes;
    private Integer totalParticipantes;
}

