package com.julian.neolearn.neolearn.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.julian.neolearn.neolearn.entity.MensajeChat.TipoMensaje;

import lombok.Data;

@Data
public class MensajeChatDTO {
private Long cveMensajeChat;
    private Long claseEnVivoId;
    private String codigoSala;
    private Long usuarioId;
    private String usuarioNombre;
    private String usuarioAvatar;
    private String contenido;
    private LocalDateTime timestamp;
    private TipoMensaje tipoMensaje;
    private Boolean editado;
    private LocalDateTime fechaEdicion;
    private Boolean esMio;
 

 }