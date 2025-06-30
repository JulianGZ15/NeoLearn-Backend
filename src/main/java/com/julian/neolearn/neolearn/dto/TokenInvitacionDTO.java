package com.julian.neolearn.neolearn.dto;

import lombok.Data;

@Data
public class TokenInvitacionDTO {
    private Long cveToken;
    private String token;
    private Boolean usado; // Puede ser "pendiente", "aceptado" o "rechazado"
    private String fechaCreacion; // Fecha de creación del token
    private String fechaExpiracion; // Fecha de expiración del token

}
