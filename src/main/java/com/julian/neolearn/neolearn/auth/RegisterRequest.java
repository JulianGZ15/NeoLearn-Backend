package com.julian.neolearn.neolearn.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    String correo;
    String password;
    String nombre;
    Long telefono;
    String direccionCompleta;
    String calle;
    String colonia;
    String ciudad;
    String estado;
    String codigoPostal;
    String pais;

    // Coordenadas geográficas
    Double latitud;
    Double longitud;

    // Identificadores de Google
    String placeId;
}
