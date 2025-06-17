package com.julian.neolearn.neolearn.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.julian.neolearn.neolearn.auth.AuthService;
import com.julian.neolearn.neolearn.auth.RegisterRequest;
import com.julian.neolearn.neolearn.dto.TokenInvitacionDTO;
import com.julian.neolearn.neolearn.service.InvitacionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/invitaciones")
@RequiredArgsConstructor
public class InvitacionController {

    private final InvitacionService invitacionService;
    private final AuthService registroService;

    @PostMapping("/generar")
    public ResponseEntity<String> generarInvitacion() {
        String url = invitacionService.generarTokenInvitacion();
        return ResponseEntity.ok(url);
    }

    @PostMapping("/registrar")
    public ResponseEntity<Void> registrar(@RequestBody RegisterRequest dto,
                                          @RequestParam String token) {
        registroService.registrarUsuarioConToken(dto, token);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

   @GetMapping
public ResponseEntity<List<TokenInvitacionDTO>> obtenerInvitacionesPorEmpresa() {
    List<TokenInvitacionDTO> tokens = invitacionService.obtenerTokensPorEmpresa();
    if (tokens.isEmpty()) {
        return ResponseEntity.noContent().build(); // o .notFound() si prefieres
    } else {
        return ResponseEntity.ok(tokens); // ← Aquí devuelves la lista completa
    }
}


}
