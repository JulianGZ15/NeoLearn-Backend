package com.julian.neolearn.neolearn.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.julian.neolearn.neolearn.dto.TokenInvitacionDTO;
import com.julian.neolearn.neolearn.entity.Empresa;
import com.julian.neolearn.neolearn.entity.TokenInvitacionEmpresa;
import com.julian.neolearn.neolearn.entity.Usuario;
import com.julian.neolearn.neolearn.mapper.TokenInvitacionMapper;
import com.julian.neolearn.neolearn.repository.EmpresaRepository;
import com.julian.neolearn.neolearn.repository.TokenInvitacionEmpresaRepository;
import com.julian.neolearn.neolearn.repository.UsuarioRepository;
import com.julian.neolearn.neolearn.service.InvitacionService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InvitacionServiceImpl implements InvitacionService{

    
    private final EmpresaRepository empresaRepository;
    private final TokenInvitacionEmpresaRepository tokenRepository;
    private final UsuarioRepository usuarioRepository;
    private final TokenInvitacionMapper tokenMapper;

    @Override
    public String generarTokenInvitacion() {
        String correo = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Supongamos que un usuario empresarial solo administra una empresa
        Empresa empresa = empresaRepository.findByUsuariosContaining(usuario)
                .orElseThrow(() -> new RuntimeException("Empresa no encontrada para el usuario"));

        String token = UUID.randomUUID().toString();


        TokenInvitacionEmpresa invitacion = new TokenInvitacionEmpresa();
        invitacion.setToken(token);
        invitacion.setEmpresa(empresa);
        invitacion.setFechaExpiracion(LocalDateTime.now().plusDays(3)); // Token válido por 3 días
        invitacion.setUsado(false);

        tokenRepository.save(invitacion);

        return "http://localhost:8080/api/invitaciones/registrar?token=" + token;
    }

    @Override
    public List<TokenInvitacionDTO> obtenerTokensPorEmpresa() {
        String correo = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Supongamos que un usuario empresarial solo administra una empresa
        Empresa empresa = empresaRepository.findByUsuariosContaining(usuario)
                .orElseThrow(() -> new RuntimeException("Empresa no encontrada para el usuario"));

        List<TokenInvitacionEmpresa> tokens = tokenRepository.findByEmpresaCveEmpresa(empresa.getCveEmpresa());

        return tokens.stream()
        .map(tokenMapper::toDTO)
        .toList();
    }
}
