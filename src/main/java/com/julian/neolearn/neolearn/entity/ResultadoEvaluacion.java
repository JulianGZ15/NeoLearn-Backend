package com.julian.neolearn.neolearn.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ResultadoEvaluacion")
public class ResultadoEvaluacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cveResultadoEvaluacion;

    @ManyToOne
    @JoinColumn(name = "cve_evaluacion")
    private Evaluacion evaluacion;

    @ManyToOne
    @JoinColumn(name = "cve_usuario")
    private Usuario usuario;

    @OneToMany(mappedBy = "resultadoEvaluacion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RespuestaEvaluacion> respuestas;


    private BigDecimal calificacion;

    private LocalDateTime fecha;
}
