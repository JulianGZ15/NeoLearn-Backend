package com.julian.neolearn.neolearn.entity;

import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "respuesta_evaluacion")
public class RespuestaEvaluacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cveRespuestaEvaluacion;

    private Character respuestaUsuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cve_resultado_evaluacion", nullable = false)
    private ResultadoEvaluacion resultadoEvaluacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cve_pregunta_evaluacion", nullable = false)
    private PreguntaEvaluacion preguntaEvaluacion;
}
