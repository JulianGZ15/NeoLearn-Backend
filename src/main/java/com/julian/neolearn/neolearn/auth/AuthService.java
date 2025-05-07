package com.julian.neolearn.neolearn.auth;

import java.time.LocalDateTime;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.julian.neolearn.neolearn.entity.Empresa;
import com.julian.neolearn.neolearn.entity.TokenInvitacionEmpresa;
import com.julian.neolearn.neolearn.entity.Usuario;
import com.julian.neolearn.neolearn.entity.Usuario.TipoUsuario;
import com.julian.neolearn.neolearn.jwt.JwtService;
import com.julian.neolearn.neolearn.repository.EmpresaRepository;
import com.julian.neolearn.neolearn.repository.TokenInvitacionEmpresaRepository;
import com.julian.neolearn.neolearn.repository.UsuarioRepository;
import com.julian.neolearn.neolearn.service.impl.UserDetailsImpl;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final TokenInvitacionEmpresaRepository tokenRepository;
    private final EmpresaRepository empresaRepository;


    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getCorreo(), request.getPassword())
        );

        UserDetails user = usuarioRepository.findByCorreo(request.getCorreo())
            .map(UserDetailsImpl::new)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        String token = jwtService.getToken(user);

        return AuthResponse.builder()
            .token(token)
            .build();
    }

    public AuthResponse register(RegisterRequest request) {
        Usuario usuario = Usuario.builder()
            .nombre(request.getNombre())
            .correo(request.getCorreo())
            .contrasena(passwordEncoder.encode(request.getPassword()))
            .build();

        usuarioRepository.save(usuario);

        String token = jwtService.getToken(new UserDetailsImpl(usuario));

        return AuthResponse.builder()
            .token(token)
            .build();
    }

    public void registrarUsuarioConToken(RegisterRequest dto, String token) {
        TokenInvitacionEmpresa invitacion = tokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Token inválido"));

        if (invitacion.isUsado() || invitacion.getFechaExpiracion().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token expirado o ya usado");
        }

        Usuario nuevo = new Usuario();
        nuevo.setCorreo(dto.getCorreo());
        nuevo.setNombre(dto.getNombre());
        nuevo.setContrasena(passwordEncoder.encode(dto.getPassword()));
        nuevo.setTipo(TipoUsuario.FINAL);
        usuarioRepository.save(nuevo);

        Empresa empresa = invitacion.getEmpresa();
        empresa.getUsuarios().add(nuevo);
        empresaRepository.save(empresa);

        invitacion.setUsado(true);
        tokenRepository.save(invitacion);
    }
}
