package com.julian.neolearn.neolearn.controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.julian.neolearn.neolearn.dto.SalaEnVivoDTO;
import com.julian.neolearn.neolearn.service.SalaEnVivoService;
import com.julian.neolearn.neolearn.webSocket.WebSocketSignalingHandler;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/api/salas-en-vivo")
@RequiredArgsConstructor
public class SalaEnVivoController {

    private final SalaEnVivoService salaService;
    private final WebSocketSignalingHandler signalingHandler;

    @PostMapping("/crear")
    public ResponseEntity<SalaEnVivoDTO> crearSala(@RequestParam Long idCurso) {
        return ResponseEntity.ok(salaService.crearSala(idCurso));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SalaEnVivoDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(salaService.obtenerPorId(id));
    }

    @GetMapping("/curso/{idCurso}")
    public ResponseEntity<List<SalaEnVivoDTO>> obtenerPorCurso(@PathVariable Long idCurso) {
        return ResponseEntity.ok(salaService.obtenerPorCurso(idCurso));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarSala(@PathVariable Long id) {
        salaService.eliminarSala(id);
        return ResponseEntity.noContent().build();
    }

    
    @GetMapping("/{codigoSala}/participantes")
    public ResponseEntity<Map<String, Object>> obtenerParticipantes(@PathVariable String codigoSala) {
        Map<String, Integer> estadisticas = signalingHandler.getEstadisticasSalas();
        int participantes = estadisticas.getOrDefault(codigoSala, 0);
        
        Map<String, Object> response = new HashMap<>();
        response.put("codigoSala", codigoSala);
        response.put("participantesConectados", participantes);
        response.put("timestamp", LocalDateTime.now());
        
        return ResponseEntity.ok(response);
    }
}
