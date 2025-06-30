package com.julian.neolearn.neolearn.controller;

import java.io.IOException;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.julian.neolearn.neolearn.dto.ConfiguracionDTO;
import com.julian.neolearn.neolearn.service.ConfiguracionService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/configuracion-certificados")
@RequiredArgsConstructor
public class ConfiguracionController {

    private final ConfiguracionService configuracionService;

@PostMapping("/guardar-firmante")
public ResponseEntity<ConfiguracionDTO> guardarFirmante(@RequestBody Map<String, String> request) {
    String firmante = request.get("firmante");
    ConfiguracionDTO configuracion = configuracionService.guardarFirmante(firmante);
    return ResponseEntity.ok(configuracion);
}


    @PostMapping("/guardar-firma")
    public ResponseEntity<?> subirFirma(@RequestParam("file") MultipartFile file) {
        try {
            ConfiguracionDTO configuracion = configuracionService.guardarFirma(file);
            return ResponseEntity.ok(configuracion);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al procesar el archivo: " + e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/guardar-logo")
    public ResponseEntity<?> subirLogo(@RequestParam("file") MultipartFile file) {
        try {
            ConfiguracionDTO configuracion = configuracionService.guardarLogo(file);
            return ResponseEntity.ok(configuracion);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al procesar el archivo: " + e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/{cveEmpresa}")
    public ResponseEntity<ConfiguracionDTO> obtenerConfiguracion(@PathVariable Long cveEmpresa) {
        try {
            ConfiguracionDTO configuracion = configuracionService.obtenerConfiguracionPorEmpresa(cveEmpresa);
            return ResponseEntity.ok(configuracion);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
