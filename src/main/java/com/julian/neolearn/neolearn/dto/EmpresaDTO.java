package com.julian.neolearn.neolearn.dto;

import java.time.LocalDate;
import java.util.List;

import com.julian.neolearn.neolearn.entity.Empresa.TipoPlan;

import lombok.Data;

@Data
public class EmpresaDTO {
    private Long cveEmpresa;
    private String nombre;
    private TipoPlan tipo_plan;
    private LocalDate fecha_inicio_plan;
    private LocalDate fecha_fin_plan;
    private Boolean esta_activo;
    private List<Long> cveUsuarios; // Solo IDs    
    
}
