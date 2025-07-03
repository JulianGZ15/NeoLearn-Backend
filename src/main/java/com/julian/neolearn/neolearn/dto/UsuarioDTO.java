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
    private Long telefono;
    private String direccionCompleta;
    private String calle;
    private String colonia;
    private String ciudad;
    private String estado;
    private String codigoPostal;
    private String pais;
    private Double latitud;
    private Double longitud;
    private String placeId;
    private String googleAddressComponents;

}

