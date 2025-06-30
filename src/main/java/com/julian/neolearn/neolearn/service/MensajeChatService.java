package com.julian.neolearn.neolearn.service;

import java.time.LocalDateTime;
import java.util.List;

import com.julian.neolearn.neolearn.dto.MensajeChatDTO;
import com.julian.neolearn.neolearn.requestandresponse.EnviarMensajeRequest;
import com.julian.neolearn.neolearn.requestandresponse.HistorialChatResponse;

public interface MensajeChatService {
    MensajeChatDTO enviarMensaje(Long claseId, Long usuarioId, EnviarMensajeRequest request);

    HistorialChatResponse obtenerHistorial(Long claseId, int pagina, int tamaño, Long usuarioActualId);

    HistorialChatResponse obtenerHistorialPorCodigoSala(String codigoSala, int pagina, int tamaño,
            Long usuarioActualId);

    MensajeChatDTO editarMensaje(Long mensajeId, String nuevoContenido, Long usuarioId);

    void eliminarMensaje(Long mensajeId, Long usuarioId);

    MensajeChatDTO enviarMensajeSistema(Long claseId, String contenido);

    Long contarMensajes(Long claseId);

    void limpiarChatClase(Long claseId, Long usuarioId);

    List<MensajeChatDTO> obtenerMensajesRecientes(Long claseId, LocalDateTime desde, Long usuarioActualId);

    boolean puedeParticiparEnChat(Long claseId, Long usuarioId);

}
