package com.julian.neolearn.neolearn.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.MalformedURLException;
import org.springframework.http.HttpHeaders;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.http.MediaType;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/archivos")
@RequiredArgsConstructor
public class ArchivoController {

    @GetMapping("/logo/{nombreArchivo}")
    public ResponseEntity<Resource> obtenerLogo(@PathVariable String nombreArchivo) throws MalformedURLException {
        Path ruta = Paths.get("uploads/certificados/configuracion").resolve(nombreArchivo).normalize();
        Resource recurso = new UrlResource(ruta.toUri());
        
        if (!recurso.exists()) {
            return ResponseEntity.notFound().build();
        }
        
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + nombreArchivo + "\"")
            .contentType(MediaType.IMAGE_JPEG) // Ajusta según el tipo
            .body(recurso);
    }

    @GetMapping("/firma/{nombreArchivo}")
    public ResponseEntity<Resource> obtenerFirma(@PathVariable String nombreArchivo) throws MalformedURLException {
        Path ruta = Paths.get("uploads/certificados/configuracion").resolve(nombreArchivo).normalize();
        Resource recurso = new UrlResource(ruta.toUri());
        
        if (!recurso.exists()) {
            return ResponseEntity.notFound().build();
        }
        
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + nombreArchivo + "\"")
            .contentType(MediaType.IMAGE_JPEG) // Ajusta según el tipo
            .body(recurso);
    }
}
