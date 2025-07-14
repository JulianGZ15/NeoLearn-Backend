package com.julian.neolearn.neolearn.entity;

import java.util.List;

import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "preguntaevaluacion")
public class PreguntaEvaluacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cve_preguntaEvaluacion")
    private Long cvePreguntaEvaluacion;

    @ManyToOne
    @JoinColumn(name = "cve_evaluacion")
    private Evaluacion evaluacion;

    @OneToMany(mappedBy = "preguntaEvaluacion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RespuestaEvaluacion> respuestas;


    @Column(columnDefinition = "TEXT")
    private String pregunta;

    private String opcion_a;
    private String opcion_b;
    private String opcion_c;
    private String opcion_d;

    private Character respuesta_correcta;
}
