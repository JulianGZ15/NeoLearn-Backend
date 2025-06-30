package com.julian.neolearn.neolearn.auth;

import java.time.LocalDateTime;
import java.util.ArrayList;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.julian.neolearn.neolearn.dto.EmpresaDTO;
import com.julian.neolearn.neolearn.entity.Empresa;
import com.julian.neolearn.neolearn.entity.TokenInvitacionEmpresa;
import com.julian.neolearn.neolearn.entity.Usuario;
import com.julian.neolearn.neolearn.entity.Empresa.TipoPlan;
import com.julian.neolearn.neolearn.entity.Usuario.TipoUsuario;
import com.julian.neolearn.neolearn.jwt.JwtService;
import com.julian.neolearn.neolearn.mapper.EmpresaMapper;
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
    private final EmpresaMapper empresaMapper;


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

    @Transactional
    public AuthResponse register(RegisterRequest request, EmpresaDTO dto) {
    Usuario usuario = Usuario.builder()
        .nombre(request.getNombre())
        .correo(request.getCorreo())
        .contrasena(passwordEncoder.encode(request.getPassword()))
        .telefono(request.getTelefono())
        .direccionCompleta(request.getDireccionCompleta())
        .calle(request.getCalle())
        .colonia(request.getColonia())
        .ciudad(request.getCiudad())
        .estado(request.getEstado())
        .codigoPostal(request.getCodigoPostal())
        .pais(request.getPais())
        .latitud(request.getLatitud())
        .longitud(request.getLongitud())
        .placeId(request.getPlaceId())
        .tipo(TipoUsuario.EMPRESARIAL)
        .build();

    // ✅ Guardar usuario primero
    usuario = usuarioRepository.save(usuario);

    Empresa empresa = empresaMapper.toEntity(dto);
    empresa.setTipo_plan(TipoPlan.PRIVADO);
    
    // ✅ Usar el método helper o verificar la lista
    if (empresa.getUsuarios() == null) {
        empresa.setUsuarios(new ArrayList<>());
    }
    empresa.getUsuarios().add(usuario);
    
    // ✅ Guardar empresa
    empresa = empresaRepository.save(empresa);

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

Usuario nuevo = Usuario.builder()
            .nombre(dto.getNombre())
            .correo(dto.getCorreo())
            .contrasena(passwordEncoder.encode(dto.getPassword()))
            .telefono(dto.telefono)
            .direccionCompleta(dto.direccionCompleta)
            .calle(dto.calle)
            .colonia(dto.colonia)
            .ciudad(dto.ciudad)
            .estado(dto.estado)
            .codigoPostal(dto.codigoPostal)
            .pais(dto.pais)
            .latitud(dto.latitud)
            .longitud(dto.longitud)
            .placeId(dto.placeId)
            .build();
        nuevo.setTipo(TipoUsuario.FINAL);
        usuarioRepository.save(nuevo);

        Empresa empresa = invitacion.getEmpresa();
        empresa.getUsuarios().add(nuevo);
        empresaRepository.save(empresa);

        invitacion.setUsado(true);
        tokenRepository.save(invitacion);
    }
}
