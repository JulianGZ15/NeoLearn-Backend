package com.julian.neolearn.neolearn.service;


import java.util.List;
import java.util.Optional;

import com.julian.neolearn.neolearn.dto.UsuarioDTO;

public interface UsuarioService {
    List<UsuarioDTO> findAll();
    Optional<UsuarioDTO> findById(Long id);
    Optional<UsuarioDTO> findByCorreo(String correo);
    UsuarioDTO findUser();
    UsuarioDTO save(UsuarioDTO usuarioDTO);
    
}
