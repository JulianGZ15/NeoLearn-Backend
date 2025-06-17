package com.julian.neolearn.neolearn.controller;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.julian.neolearn.neolearn.dto.CertificadoDTO;
import com.julian.neolearn.neolearn.service.CertificadoService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/certificados")
@RequiredArgsConstructor
public class CertificadoController {

    private final CertificadoService certificadoService;

    @PostMapping("/{cveInscripcion}")
    public ResponseEntity<CertificadoDTO> generar(@PathVariable Long cveInscripcion) throws Exception {
        return ResponseEntity.ok(certificadoService.generarCertificado(cveInscripcion));
    }

    @GetMapping("/archivo/{nombreArchivo}")
    public ResponseEntity<Resource> obtenerArchivo(@PathVariable String nombreArchivo) throws MalformedURLException {
        Path ruta = Paths.get("uploads/certificados").resolve(nombreArchivo).normalize();
        Resource recurso = new UrlResource(ruta.toUri());
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + nombreArchivo + "\"")
            .contentType(MediaType.APPLICATION_PDF)
            .body(recurso);
    }
}
