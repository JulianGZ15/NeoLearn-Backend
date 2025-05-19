package com.julian.neolearn.neolearn.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<Map<String, Object>> obtenerResumen(@RequestParam Long cveEmpresa) {
        Map<String, Object> resumen = estadisticasService.obtenerResumenEstadisticas(cveEmpresa);
        return ResponseEntity.ok(resumen);
    }


}