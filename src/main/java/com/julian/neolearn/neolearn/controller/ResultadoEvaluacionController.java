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

import com.julian.neolearn.neolearn.dto.ResultadoEvaluacionDTO;
import com.julian.neolearn.neolearn.service.ResultadoEvaluacionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/evaluaciones/resultados")
@RequiredArgsConstructor
public class ResultadoEvaluacionController {
    private final ResultadoEvaluacionService resultadoEvaluacionService;



    @GetMapping("/evaluacion/{cveEvaluacion}")
    public ResponseEntity<List<ResultadoEvaluacionDTO>> listarResultadosPorEvaluacion(@PathVariable Long cveEvaluacion) {
        List<ResultadoEvaluacionDTO> resultados = resultadoEvaluacionService.buscarResultadoEvaluacionPorEvaluacion(cveEvaluacion);
        return ResponseEntity.ok(resultados);
    }

    @PostMapping
    public ResponseEntity<ResultadoEvaluacionDTO> crearResultadoEvaluacion(@RequestBody ResultadoEvaluacionDTO dto) {
        ResultadoEvaluacionDTO nuevoResultado = resultadoEvaluacionService.guardarResultadoEvaluacion(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoResultado);
    }

    @PutMapping
    public ResponseEntity<ResultadoEvaluacionDTO> actualizarResultadoEvaluacion(@RequestBody ResultadoEvaluacionDTO dto) {
        ResultadoEvaluacionDTO resultado = resultadoEvaluacionService.guardarResultadoEvaluacion(dto);
        return ResponseEntity.ok(resultado);
    }

    @DeleteMapping("/{cveResultadoevaluacion}")
    public ResponseEntity<Void> eliminarResultadoEvaluacion(@PathVariable Long cveResultadoevaluacion) {
        resultadoEvaluacionService.borrarResultadoEvaluacionPorId(cveResultadoevaluacion);
        return ResponseEntity.noContent().build();
    }

}
