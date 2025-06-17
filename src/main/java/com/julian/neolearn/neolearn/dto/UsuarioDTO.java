package com.julian.neolearn.neolearn.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class UsuarioDTO {
    private Long cveUsuario;
    private String contrasena;
    private String nombre;
    private String correo;
    private String tipo;
    private LocalDate fecha_registro;
    private String fotoperfil;
}

