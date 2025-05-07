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

import com.julian.neolearn.neolearn.dto.EvaluacionDTO;
import com.julian.neolearn.neolearn.service.EvaluacionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/evaluaciones")
@RequiredArgsConstructor
public class EvaluacionController {

    private final EvaluacionService evaluacionService;

    @GetMapping("/curso/{cveCurso}")
    public ResponseEntity<List<EvaluacionDTO>> listarEvaluacionesPorCurso(@PathVariable Long cveCurso) {
        List<EvaluacionDTO> evaluaciones = evaluacionService.listarEvaluacionesPorCurso(cveCurso);
        return ResponseEntity.ok(evaluaciones);
    }

    @GetMapping("/{cveEvaluacion}")
    public ResponseEntity<EvaluacionDTO> obtenerEvaluacionPorId(@PathVariable Long cveEvaluacion) {
        return evaluacionService.buscarEvaluacionPorId(cveEvaluacion)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{cveCurso}")
    public ResponseEntity<EvaluacionDTO> crearEvaluacion(@PathVariable Long cveCurso, @RequestBody EvaluacionDTO dto) {
        EvaluacionDTO nuevaEvaluacion = evaluacionService.guardarEvaluacion(dto, cveCurso);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaEvaluacion);
    }

    @PutMapping("/{cveCurso}")
    public ResponseEntity<EvaluacionDTO> actualizarEvaluacion(@PathVariable Long cveCurso, @RequestBody EvaluacionDTO dto) {
        EvaluacionDTO evaluacion = evaluacionService.guardarEvaluacion(dto, cveCurso);
        return ResponseEntity.ok(evaluacion);
    }

    @DeleteMapping("/{cveEvaluacion}")
    public ResponseEntity<Void> eliminarEvaluacion(@PathVariable Long cveEvaluacion) {
        evaluacionService.borrarEvaluacionPorId(cveEvaluacion);
        return ResponseEntity.noContent().build();

    }

}
