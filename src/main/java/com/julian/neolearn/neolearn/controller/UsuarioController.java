package com.julian.neolearn.neolearn.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.julian.neolearn.neolearn.dto.UsuarioDTO;
import com.julian.neolearn.neolearn.service.UsuarioService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {
    private final UsuarioService usuarioService;


    @GetMapping("/{cveUsuario}")
    public ResponseEntity<UsuarioDTO> obtenerUsuarioPorId(@PathVariable Long cveUsuario) {
        return usuarioService.findById(cveUsuario)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<UsuarioDTO> obtenerUsuario() {
        UsuarioDTO usuario = usuarioService.findUser();
        return ResponseEntity.ok(usuario);
    }

    @PutMapping
    public ResponseEntity<UsuarioDTO> actualizarUsuario(@RequestBody UsuarioDTO usuario){
        usuarioService.save(usuario);
        return ResponseEntity.ok(usuario);
    }

}
