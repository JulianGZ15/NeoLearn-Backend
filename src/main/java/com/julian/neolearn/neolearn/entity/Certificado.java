package com.julian.neolearn.neolearn.entity;

import java.time.LocalDate;

import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Certificado")
public class Certificado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cve_certificado;

    @ManyToOne
    @JoinColumn(name = "cve_inscripcion")
    private Inscripcion inscripcion;

    @Column(columnDefinition = "TEXT")
    private String nombreArchivo;

    private LocalDate fecha_emision;
}

