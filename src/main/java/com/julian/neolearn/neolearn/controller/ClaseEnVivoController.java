package com.julian.neolearn.neolearn.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.julian.neolearn.neolearn.dto.ClaseEnVivoDTO;
import com.julian.neolearn.neolearn.service.ClaseEnVivoService;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/api/clases-en-vivo")
@RequiredArgsConstructor
public class ClaseEnVivoController {
    
    private final ClaseEnVivoService claseEnVivoService;
    
    @GetMapping("/curso/{cursoId}")
    public ResponseEntity<List<ClaseEnVivoDTO>> listarClasesPorCurso(@PathVariable Long cursoId) {
        List<ClaseEnVivoDTO> clases = claseEnVivoService.listarClasesEnVivo(cursoId);
        return ResponseEntity.ok(clases);
    }
    
    @PostMapping
    public ResponseEntity<ClaseEnVivoDTO> crearClase(@RequestBody ClaseEnVivoDTO claseDTO) {
        ClaseEnVivoDTO nuevaClase = claseEnVivoService.guardarClaseEnVivo(claseDTO);
        return ResponseEntity.ok(nuevaClase);
    }
    

}
