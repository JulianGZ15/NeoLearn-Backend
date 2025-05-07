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

import com.julian.neolearn.neolearn.dto.PreguntaDTO;
import com.julian.neolearn.neolearn.service.PreguntaService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/preguntas")
@RequiredArgsConstructor
public class PreguntaController {

    private final PreguntaService preguntaService;

    @GetMapping("/{cvePregunta}")
    public ResponseEntity<PreguntaDTO> obtenerPreguntaPorId(@PathVariable Long cvePregunta) {
        return preguntaService.buscarPreguntaPorId(cvePregunta)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/curso/{cveCurso}")
    public ResponseEntity<List<PreguntaDTO>> listarPreguntasPorCurso(@PathVariable Long cveCurso) {
        List<PreguntaDTO> preguntas = preguntaService.listarPreguntasPorCurso(cveCurso);
        return ResponseEntity.ok(preguntas);
    }

    @PostMapping("/{cveCurso}")
    public ResponseEntity<PreguntaDTO> crearPregunta(@PathVariable Long cveCurso, @RequestBody PreguntaDTO dto) {
        PreguntaDTO nuevaPregunta = preguntaService.guardarPregunta(dto, cveCurso);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaPregunta);
    }

    @PutMapping("/{cveCurso}")
    public ResponseEntity<PreguntaDTO> actualizarPregunta(@PathVariable Long cveCurso, @RequestBody PreguntaDTO dto) {
        PreguntaDTO pregunta = preguntaService.guardarPregunta(dto, cveCurso);
        return ResponseEntity.ok(pregunta);
    }

    @DeleteMapping("/{cvePregunta}")
    public ResponseEntity<Void> eliminarPregunta(@PathVariable Long cvePregunta) {
        preguntaService.borrarPreguntaPorId(cvePregunta);
        return ResponseEntity.noContent().build();
    }
}

