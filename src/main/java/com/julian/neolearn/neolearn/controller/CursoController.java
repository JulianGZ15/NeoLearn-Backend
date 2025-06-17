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

import com.julian.neolearn.neolearn.dto.CursoDTO;
import com.julian.neolearn.neolearn.service.CursoService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/cursos")
@RequiredArgsConstructor
public class CursoController {

    private final CursoService cursoService;

    @GetMapping
    public ResponseEntity<List<CursoDTO>> listarCursos() {
        List<CursoDTO> cursos = cursoService.listarCursos();
        return ResponseEntity.ok(cursos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CursoDTO> obtenerCursoPorId(@PathVariable Long id) {
        return cursoService.buscarCursoPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<CursoDTO> crearCurso(@RequestBody CursoDTO dto) {
        CursoDTO nuevoCurso = cursoService.guardarCurso(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoCurso);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CursoDTO> actualizarCurso(@PathVariable Long id, @RequestBody CursoDTO dto) {
        dto.setCveCurso(id);
        CursoDTO cursoActualizada = cursoService.guardarCurso(dto);
        return ResponseEntity.ok(cursoActualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCurso(@PathVariable Long id) {
        cursoService.borrarCursoPorId(id);
        return ResponseEntity.noContent().build();
    }

    

    // Subir portada
    @PostMapping("/{cursoId}/portada")
    public ResponseEntity<CursoDTO> subirPortada(
            @PathVariable Long cursoId,
            @RequestParam("file") MultipartFile file) {
        try {
            CursoDTO cursoDTO = cursoService.guardarPortada(cursoId, file);
            return ResponseEntity.ok(cursoDTO);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body(null);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                 .body(null);
        }
    }

    // Obtener portada
    @GetMapping("/portada/{nombreArchivo:.+}")
    public ResponseEntity<Resource> obtenerPortada(@PathVariable String nombreArchivo) {
        try {
            Resource recurso = cursoService.obtenerPortada(nombreArchivo);
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG) // o detectar tipo MIME dinámicamente si deseas
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + recurso.getFilename() + "\"")
                    .body(recurso);
        } catch (MalformedURLException e) {
            return ResponseEntity.notFound().build();
        }
    }


}

