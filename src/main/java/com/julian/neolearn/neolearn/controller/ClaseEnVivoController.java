package com.julian.neolearn.neolearn.controller;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.julian.neolearn.neolearn.dto.ClaseEnVivoDTO;
import com.julian.neolearn.neolearn.dto.ProgramarClaseRequest;
import com.julian.neolearn.neolearn.entity.Usuario;
import com.julian.neolearn.neolearn.repository.UsuarioRepository;
import com.julian.neolearn.neolearn.service.ClaseEnVivoService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/clases-en-vivo")
@RequiredArgsConstructor
public class ClaseEnVivoController {

    private final ClaseEnVivoService claseService;
    private final UsuarioRepository usuarioRepository;

    @PostMapping("/programar")
    public ResponseEntity<ClaseEnVivoDTO> programarClase(
            @RequestBody ProgramarClaseRequest request,
            @RequestParam Long idSala,
            Principal principal) {
        Long idInstructor = obtenerIdDesdeJWT(principal);
        ClaseEnVivoDTO clase = claseService.programarClase(request, idSala, idInstructor);
        return ResponseEntity.status(HttpStatus.CREATED).body(clase);
    }

    @PutMapping("/{id}/reprogramar")
    public ResponseEntity<ClaseEnVivoDTO> reprogramarClase(
            @PathVariable Long id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime nuevaFecha) {
        return ResponseEntity.ok(claseService.reprogramarClase(id, nuevaFecha));
    }

    @DeleteMapping("/{id}/cancelar")
    public ResponseEntity<Void> cancelarClase(@PathVariable Long id) {
        claseService.cancelarClase(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/iniciar")
    public ResponseEntity<ClaseEnVivoDTO> iniciarTransmision(@PathVariable Long id) {
        return ResponseEntity.ok(claseService.iniciarTransmision(id));
    }

    @PutMapping("/{id}/finalizar")
    public ResponseEntity<ClaseEnVivoDTO> finalizarTransmision(@PathVariable Long id) {
        return ResponseEntity.ok(claseService.finalizarTransmision(id));
    }

    @GetMapping("/curso/{idCurso}")
    public ResponseEntity<List<ClaseEnVivoDTO>> listarPorCurso(@PathVariable Long idCurso) {
        return ResponseEntity.ok(claseService.listarPorCurso(idCurso));
    }

    @GetMapping("/curso/{idCurso}/fecha/{fecha}")
    public ResponseEntity<List<ClaseEnVivoDTO>> listarPorCursoYFecha(
            @PathVariable Long idCurso,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        return ResponseEntity.ok(claseService.listarClasesProgramadas(idCurso, fecha));
    }

    @GetMapping("/instructor/mis-clases")
    public ResponseEntity<List<ClaseEnVivoDTO>> misClases(Principal principal) {
        Long idInstructor = obtenerIdDesdeJWT(principal);
        return ResponseEntity.ok(claseService.listarPorInstructor(idInstructor));
    }

    @GetMapping("/en-vivo")
    public ResponseEntity<List<ClaseEnVivoDTO>> clasesEnVivo() {
        return ResponseEntity.ok(claseService.listarClasesEnVivo());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClaseEnVivoDTO> obtenerDetalle(@PathVariable Long id) {
        return claseService.obtenerDetalle(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/sala/{idSala}/unirse")
    public ResponseEntity<Void> unirseASala(@PathVariable Long idSala, Principal principal) {
        Long idUsuario = obtenerIdDesdeJWT(principal);
        claseService.agregarParticipante(idSala, idUsuario);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/sala/{idSala}/salir")
    public ResponseEntity<Void> salirDeSala(@PathVariable Long idSala, Principal principal) {
        Long idUsuario = obtenerIdDesdeJWT(principal);
        claseService.removerParticipante(idSala, idUsuario);
        return ResponseEntity.ok().build();
    }

    private Long obtenerIdDesdeJWT(Principal principal) {
String correo = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return usuario.getCveUsuario();
    }
}
