package com.julian.neolearn.neolearn.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.julian.neolearn.neolearn.dto.CursoVendidoDTO;
import com.julian.neolearn.neolearn.dto.SuscripcionMensualDTO;
import com.julian.neolearn.neolearn.dto.UsuarioDTO;
import com.julian.neolearn.neolearn.service.EstadisticasEmpresaService;

import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/estadisticas")
@AllArgsConstructor
public class EstadisticasEmpresaController {

    private final EstadisticasEmpresaService estadisticasService;

    @GetMapping("/resumen")
    public ResponseEntity<Map<String, Object>> obtenerResumen() {
        Map<String, Object> resumen = estadisticasService.obtenerResumenEstadisticas();
        return ResponseEntity.ok(resumen);
    }

    @GetMapping("/cursos/total")
    public ResponseEntity<Long> contarCursosPorEmpresa() {
        Long total = estadisticasService.contarCursosPorEmpresa();
        return ResponseEntity.ok(total);
    }

    @GetMapping("/ganancias/mes-actual")
    public ResponseEntity<Double> calcularGananciasMesActual() {
        Double ganancias = estadisticasService.calcularGananciasMesActual();
        return ResponseEntity.ok(ganancias);
    }

    @GetMapping("/suscripciones/total")
    public ResponseEntity<Long> contarTotalSuscripciones() {
        Long total = estadisticasService.contarTotalSuscripciones();
        return ResponseEntity.ok(total);
    }

    @GetMapping("/estudiantes/total")
    public ResponseEntity<Long> contarEstudiantesEmpresa() {
        Long total = estadisticasService.contarEstudiantesEmpresa();
        return ResponseEntity.ok(total);
    }

    @GetMapping("/suscriptores/ultimos-cinco")
    public ResponseEntity<List<UsuarioDTO>> encontrarUltimosCincoSuscriptores() {
        List<UsuarioDTO> suscriptores = estadisticasService.encontrarUltimosCincoSuscriptores();
        return ResponseEntity.ok(suscriptores);
    }

    @GetMapping("/cursos/mas-vendidos")
    public ResponseEntity<List<CursoVendidoDTO>> encontrarCursosMasVendidos() {
        List<CursoVendidoDTO> cursos = estadisticasService.encontrarCursosMasVendidos();
        return ResponseEntity.ok(cursos);
    }

    @GetMapping("/suscripciones/por-mes")
    public ResponseEntity<List<SuscripcionMensualDTO>> obtenerSuscripcionesPorMes() {
        List<SuscripcionMensualDTO> lista = estadisticasService.obtenerSuscripcionesPorMes();
        return ResponseEntity.ok(lista);
    }

}