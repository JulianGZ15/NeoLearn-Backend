package com.julian.neolearn.neolearn.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class ResultadoEvaluacionDTO {
    private Long cveResultadoEvaluacion;
    private Long cveEvaluacion;
    private Long cveUsuario;
    private BigDecimal calificacion;
    private LocalDateTime fecha;
}
