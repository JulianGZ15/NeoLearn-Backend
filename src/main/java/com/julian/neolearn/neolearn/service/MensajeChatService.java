package com.julian.neolearn.neolearn.service;

import java.util.List;
import java.util.Optional;

import com.julian.neolearn.neolearn.dto.MensajeChatDTO;

public interface MensajeChatService {

    Optional<MensajeChatDTO> buscarMensajeChatPorId(Long cveMensajeChat);
    List<MensajeChatDTO> listarMensajesChat();
    MensajeChatDTO guardarMensajeChat(MensajeChatDTO mensajeChat);
    void borrarMensajeChatPorId(Long cveMensajeChat);
    
}
