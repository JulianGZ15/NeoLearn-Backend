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

import com.julian.neolearn.neolearn.dto.CuponDTO;
import com.julian.neolearn.neolearn.service.CuponService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/cupones")
@RequiredArgsConstructor
public class CuponController {

    private final CuponService cuponService;

    @GetMapping("/{cveCurso}/cupones")
    public ResponseEntity<List<CuponDTO>> listarCupones(@PathVariable Long cveCurso) {
        List<CuponDTO> cupones = cuponService.listarCupones(cveCurso);
        return ResponseEntity.ok(cupones);
    }

    @GetMapping("/{cveCupon}")
    public ResponseEntity<CuponDTO> obtenerCuponPorId(@PathVariable Long cveCupon) {
        return cuponService.buscarCuponPorId(cveCupon)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{cveCurso}")
    public ResponseEntity<CuponDTO> crearCupon(@RequestBody CuponDTO dto, @PathVariable Long cveCurso) {
        CuponDTO nuevoCupon = cuponService.guardarCupon(dto, cveCurso);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoCupon);
    }

    @PutMapping("/{cveCurso}")
    public ResponseEntity<CuponDTO> actualizarCupon(@PathVariable Long cveCurso, @RequestBody CuponDTO dto) {
        CuponDTO cuponActualizado = cuponService.guardarCupon(dto, cveCurso);
        return ResponseEntity.ok(cuponActualizado);
    }

    @DeleteMapping("/{cveCupon}")
    public ResponseEntity<Void> borrarCuponPorId(@PathVariable Long cveCupon) {
        cuponService.borrarCuponPorId(cveCupon);
        return ResponseEntity.noContent().build();
    }
}
