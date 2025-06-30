package com.julian.neolearn.neolearn.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.julian.neolearn.neolearn.dto.UsuarioDTO;
import com.julian.neolearn.neolearn.entity.Usuario;
import com.julian.neolearn.neolearn.mapper.UsuarioMapper;
import com.julian.neolearn.neolearn.repository.UsuarioRepository;
import com.julian.neolearn.neolearn.service.UsuarioService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;

    @Transactional(readOnly = true)
    @Override
    public List<UsuarioDTO> findAll() {
        return usuarioRepository.findAll()
                .stream()
                .map(usuarioMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<UsuarioDTO> findById(Long id) {
        return usuarioRepository.findById(id)
                .map(usuarioMapper::toDto);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<UsuarioDTO> findByCorreo(String correo) {
        return usuarioRepository.findByCorreo(correo)
                .map(usuarioMapper::toDto);
    }


    @Transactional(readOnly = true)
    @Override
    public UsuarioDTO findUser(){

        String correo = SecurityContextHolder.getContext().getAuthentication().getName();

        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
                
                return usuarioMapper.toDto(usuario);
    }

@Transactional
@Override
public UsuarioDTO save(UsuarioDTO usuarioDTO) {
    if (usuarioDTO.getCveUsuario() != null) {
        // Es una actualización - cargar entidad existente
        Usuario existingUsuario = usuarioRepository.findById(usuarioDTO.getCveUsuario())
            .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));
        
        // Actualizar campos básicos sin tocar las colecciones
        existingUsuario.setNombre(usuarioDTO.getNombre());
        existingUsuario.setCorreo(usuarioDTO.getCorreo());
        existingUsuario.setTelefono(usuarioDTO.getTelefono());
        existingUsuario.setDireccionCompleta(usuarioDTO.getDireccionCompleta());
        existingUsuario.setCalle(usuarioDTO.getCalle());
        existingUsuario.setColonia(usuarioDTO.getColonia());
        existingUsuario.setCiudad(usuarioDTO.getCiudad());
        existingUsuario.setEstado(usuarioDTO.getEstado());
        existingUsuario.setCodigoPostal(usuarioDTO.getCodigoPostal());
        existingUsuario.setPais(usuarioDTO.getPais());
        existingUsuario.setLatitud(usuarioDTO.getLatitud());
        existingUsuario.setLongitud(usuarioDTO.getLongitud());
        existingUsuario.setPlaceId(usuarioDTO.getPlaceId());
        existingUsuario.setFotoperfil(usuarioDTO.getFotoperfil());
        // ... otros campos que quieras actualizar
        
        // NO tocar clasesEnVivo ni otras colecciones con orphanRemoval
        
        Usuario saved = usuarioRepository.save(existingUsuario);
        return usuarioMapper.toDto(saved);
    } else {
        // Es una creación - usar mapper normal
        Usuario usuario = usuarioMapper.toEntity(usuarioDTO);
        Usuario saved = usuarioRepository.save(usuario);
        return usuarioMapper.toDto(saved);
    }
}

}
