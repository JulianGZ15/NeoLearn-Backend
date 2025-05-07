package com.julian.neolearn.neolearn.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.julian.neolearn.neolearn.dto.InscripcionDTO;
import com.julian.neolearn.neolearn.entity.Usuario;
import com.julian.neolearn.neolearn.entity.Usuario.TipoUsuario;
import com.julian.neolearn.neolearn.repository.UsuarioRepository;
import com.julian.neolearn.neolearn.service.InscripcionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/inscripciones")
@RequiredArgsConstructor
public class InscripcionController {

    private final InscripcionService inscripcionService;
    private final UsuarioRepository usuarioRepository;

    @GetMapping("/curso/{cveCurso}")
    public ResponseEntity<List<InscripcionDTO>> listarInscripciones(@PathVariable Long cveCurso) {
        List<InscripcionDTO> inscripciones = inscripcionService.listarInscripcionesPorCurso(cveCurso);
        return ResponseEntity.ok(inscripciones);
    }

    @GetMapping("/{cveInscripcion}")
    public ResponseEntity<InscripcionDTO> obtenerInscripcionPorId(@PathVariable Long cveInscripcion) {
        return inscripcionService.buscarInscripcionPorId(cveInscripcion)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{cveCurso}")
    public ResponseEntity<InscripcionDTO> crearInscripcion(@RequestBody InscripcionDTO dto, @PathVariable Long cveCurso) {
        InscripcionDTO nuevaInscripcion = inscripcionService.guardarInscripcion(dto, cveCurso);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaInscripcion);
    }

    @PutMapping("/{cveCurso}")
    public ResponseEntity<InscripcionDTO> actualizarInscripcion(@PathVariable Long cveCurso, @RequestBody InscripcionDTO dto) {
         String correo = SecurityContextHolder.getContext().getAuthentication().getName();

        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!usuario.getTipo().equals(TipoUsuario.EMPRESARIAL)) {
            throw new RuntimeException("No tienes permiso para modificar la inscripcion");
        }

        InscripcionDTO inscripcionActualizada = inscripcionService.guardarInscripcion(dto, cveCurso); // Aquí se puede pasar el cveCurso si es necesario
        return ResponseEntity.ok(inscripcionActualizada);
    }

    @DeleteMapping("/{cveInscripcion}")
    public ResponseEntity<Void> eliminarInscripcion(@PathVariable Long cveInscripcion) {
        inscripcionService.borrarInscripcionPorId(cveInscripcion);
        return ResponseEntity.noContent().build();
     }
}
