package com.julian.neolearn.neolearn.controller;

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

import com.julian.neolearn.neolearn.dto.RespuestaDTO;
import com.julian.neolearn.neolearn.service.RespuestaService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/respuestas")
@RequiredArgsConstructor
public class RespuestaController {

    private final RespuestaService respuestaService;

    @GetMapping("/{cveRespuesta}")
    public ResponseEntity<RespuestaDTO> obtenerRespuestaPorId(@PathVariable Long cveRespuesta) {
        return respuestaService.buscarRespuestaPorId(cveRespuesta)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{cvePregunta}")
    public ResponseEntity<RespuestaDTO> crearRespuesta(@PathVariable Long cvePregunta, @RequestBody RespuestaDTO dto) {
        RespuestaDTO nuevaRespuesta = respuestaService.guardarRespuesta(dto, cvePregunta);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaRespuesta);
    }

    @PutMapping("/{cvePregunta}")
    public ResponseEntity<RespuestaDTO> actualizarRespuesta(@PathVariable Long cvePregunta, @RequestBody RespuestaDTO dto) {
        RespuestaDTO respuesta = respuestaService.guardarRespuesta(dto, cvePregunta);
        return ResponseEntity.ok(respuesta);
    }

    @DeleteMapping("/{cveRespuesta}")
    public ResponseEntity<Void> eliminarRespuesta(@PathVariable Long cveRespuesta) {
        respuestaService.borrarRespuestaPorId(cveRespuesta);
        return ResponseEntity.noContent().build();
    }

}
