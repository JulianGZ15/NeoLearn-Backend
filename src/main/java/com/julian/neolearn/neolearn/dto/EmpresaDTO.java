package com.julian.neolearn.neolearn.dto;

import java.time.LocalDate;
import java.util.List;

import com.julian.neolearn.neolearn.entity.Empresa.TipoPlan;

import lombok.Data;

@Data
public class EmpresaDTO {
    private Long cveEmpresa;
    private String nombre;
    private TipoPlan tipoPlan;
    private LocalDate fechaInicioPlan;
    private LocalDate fechaFinPlan;
    private Boolean estaActivo;
    private List<Long> cveUsuarios; // Solo IDs
}
