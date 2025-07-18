package com.julian.neolearn.neolearn.dto;

import java.time.LocalDate;
import lombok.Data;

@Data
public class CertificadoDTO {
    private Long cve_certificado;
    private Long cve_inscripcion;
    private String nombreArchivo;
    private LocalDate fecha_emision;
}
