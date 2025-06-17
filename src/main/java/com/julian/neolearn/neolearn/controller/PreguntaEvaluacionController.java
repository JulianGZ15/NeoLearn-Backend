package com.julian.neolearn.neolearn.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.julian.neolearn.neolearn.dto.PreguntaEvaluacionDTO;
import com.julian.neolearn.neolearn.service.PreguntaEvaluacionService;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("api/evaluacion/preguntas")
@RequiredArgsConstructor
public class PreguntaEvaluacionController {

    private final PreguntaEvaluacionService preguntaEvaluacionService;


    @GetMapping("/{cvePreguntaEvaluacion}")
    public ResponseEntity<PreguntaEvaluacionDTO> obtenerPreguntaPorId(@PathVariable Long cvePreguntaEvaluacion) {
        return preguntaEvaluacionService.buscarPreguntaEvaluacionPorId(cvePreguntaEvaluacion)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @GetMapping("/evaluacion/{cveEvaluacion}")
    public ResponseEntity<List<PreguntaEvaluacionDTO>> listarPreguntasPorEvaluacion(@PathVariable Long cveEvaluacion) {
        List<PreguntaEvaluacionDTO> preguntas = preguntaEvaluacionService.listarPreguntasPorEvaluacion(cveEvaluacion);
        return ResponseEntity.ok(preguntas);
    }


    @PostMapping("/{cveEvaluacion}")
    public ResponseEntity<PreguntaEvaluacionDTO> crearPregunta(@PathVariable Long cveEvaluacion, @RequestBody PreguntaEvaluacionDTO dto) {
        PreguntaEvaluacionDTO nuevaPregunta = preguntaEvaluacionService.guardarPreguntaEvaluacion(dto, cveEvaluacion);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaPregunta);
    }


    @PutMapping("/{cvePreguntaEvaluacion}")
    public ResponseEntity<PreguntaEvaluacionDTO> actualizarPregunta( @RequestBody PreguntaEvaluacionDTO dto) {
        PreguntaEvaluacionDTO pregunta = preguntaEvaluacionService.actualizarPregunta(dto);
        return ResponseEntity.ok(pregunta);
    }


    @DeleteMapping("/{cvePreguntaEvaluacion}")
    public ResponseEntity<Void> eliminarPregunta(@PathVariable Long cvePreguntaEvaluacion) {
        preguntaEvaluacionService.borrarPreguntaEvaluacionPorId(cvePreguntaEvaluacion);
        return ResponseEntity.noContent().build();
     }

}
