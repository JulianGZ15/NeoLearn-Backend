package com.julian.neolearn.neolearn.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.julian.neolearn.neolearn.dto.RespuestaEvaluacionDTO;
import com.julian.neolearn.neolearn.service.RespuestasEvaluacionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/respuestas-evaluacion")
@RequiredArgsConstructor
public class RespuestasEvaluacionController {

    private final RespuestasEvaluacionService respuestasEvaluacionService;

    @PostMapping
    public ResponseEntity<RespuestaEvaluacionDTO> crearRepuestaEvaluacion(@RequestBody RespuestaEvaluacionDTO dto) {
        RespuestaEvaluacionDTO respuestaGuardada = respuestasEvaluacionService.guardarRespuestaEvaluacion(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(respuestaGuardada);

    }

    @PostMapping("/bulk")
    public ResponseEntity<List<RespuestaEvaluacionDTO>> crearRepuestaEvaluacion(
            @RequestBody List<RespuestaEvaluacionDTO> respuestas) {
        List<RespuestaEvaluacionDTO> respuestasGuardadas = respuestasEvaluacionService
                .guardarRespuestasEvaluacion(respuestas);
        return ResponseEntity.status(HttpStatus.CREATED).body(respuestasGuardadas);

    }

    @GetMapping("/resultado/{cveResultadoEvaluacion}")
    public ResponseEntity<List<RespuestaEvaluacionDTO>> buscarRespuestasPorResultadoEvaluacion(
            @PathVariable Long cveResultadoEvaluacion) {
        List<RespuestaEvaluacionDTO> respuestas = respuestasEvaluacionService
                .buscarRespuestasPorResultadoEvaluacion(cveResultadoEvaluacion);
        return ResponseEntity.ok(respuestas);
    }

    @DeleteMapping("/resultado/{cveResultadoEvaluacion}")
    public ResponseEntity<Void> borrarRespuestasPorResultadoEvaluacion(@PathVariable Long cveResultadoEvaluacion) {
        respuestasEvaluacionService.borrarRespuestasEvaluacionPoResultadoEvaluacion(cveResultadoEvaluacion);
        return ResponseEntity.noContent().build();
    }

}
