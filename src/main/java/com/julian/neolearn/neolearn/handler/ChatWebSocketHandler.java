package com.julian.neolearn.neolearn.handler;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.julian.neolearn.neolearn.dto.MensajeChatDTO;
import com.julian.neolearn.neolearn.requestandresponse.EnviarMensajeRequest;
import com.julian.neolearn.neolearn.service.MensajeChatService;

@Component
public class ChatWebSocketHandler extends TextWebSocketHandler {

    private final Map<String, List<WebSocketSession>> sesionesPorSala = new ConcurrentHashMap<>();
    private final MensajeChatService chatService;
    private final ObjectMapper objectMapper;

    public ChatWebSocketHandler(MensajeChatService chatService) {
        this.chatService = chatService;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); // 👈 necesario

    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("=== DEBUG CONEXIÓN ===");
        System.out.println("Atributos de sesión: " + session.getAttributes());

        String salaId = getSalaId(session);
        Long usuarioId = getUsuarioId(session);

        System.out.println("SalaId extraído: " + salaId);
        System.out.println("UsuarioId extraído: " + usuarioId);
        System.out.println("=====================");

        if (salaId != null && usuarioId != null) {
            sesionesPorSala.computeIfAbsent(salaId, k -> new CopyOnWriteArrayList<>()).add(session);
            System.out.println(
                    "✅ Sesión agregada a la sala " + salaId + ". Total: " + sesionesPorSala.get(salaId).size());
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String salaId = getSalaId(session);
        Long usuarioId = getUsuarioId(session);

        // ✅ VALIDAR que tenemos los datos necesarios
        if (salaId == null || usuarioId == null) {
            System.err.println("Mensaje rechazado: salaId o usuarioId es null");
            return;
        }

        try {
            JsonNode json = objectMapper.readTree(message.getPayload());
            String tipo = json.get("type").asText();

            System.out.println("Mensaje recibido - Tipo: " + tipo + ", Usuario: " + usuarioId + ", Sala: " + salaId);

            switch (tipo) {
                case "chat-message":
                    manejarMensajeChat(salaId, usuarioId, json);
                    break;
                case "typing":
                    manejarTyping(salaId, usuarioId, session);
                    break;
                case "stop-typing":
                    manejarStopTyping(salaId, usuarioId, session);
                    break;
                case "delete-message":
                    manejarEliminarMensaje(salaId, usuarioId, json);
                    break;
                case "edit-message":
                    manejarEditarMensaje(salaId, usuarioId, json);
                    break;
                default:
                    System.err.println("Tipo de mensaje no reconocido: " + tipo);
            }

        } catch (Exception e) {
            System.err.println("Error procesando mensaje de chat: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void manejarMensajeChat(String salaId, Long usuarioId, JsonNode json) {
        try {
            String contenido = json.get("contenido").asText();
            Long claseId = json.get("claseId").asLong();

            // ✅ VALIDAR contenido
            if (contenido == null || contenido.trim().isEmpty()) {
                System.err.println("Contenido del mensaje vacío");
                return;
            }

            EnviarMensajeRequest request = new EnviarMensajeRequest();
            request.setContenido(contenido);

            MensajeChatDTO mensajeDTO = chatService.enviarMensaje(claseId, usuarioId, request);

            // Broadcast del mensaje a todos en la sala
            String mensajeJson = objectMapper.writeValueAsString(Map.of(
                    "type", "new-message",
                    "mensaje", mensajeDTO));

            broadcastToSala(salaId, mensajeJson);

        } catch (Exception e) {
            System.err.println("Error enviando mensaje de chat: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void manejarTyping(String salaId, Long usuarioId, WebSocketSession senderSession) {
        try {
            String typingJson = objectMapper.writeValueAsString(Map.of(
                    "type", "user-typing",
                    "usuarioId", usuarioId));

            broadcastToSalaExcepto(salaId, typingJson, senderSession);
        } catch (Exception e) {
            System.err.println("Error manejando typing: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void manejarStopTyping(String salaId, Long usuarioId, WebSocketSession senderSession) {
        try {
            String stopTypingJson = objectMapper.writeValueAsString(Map.of(
                    "type", "user-stop-typing",
                    "usuarioId", usuarioId));

            broadcastToSalaExcepto(salaId, stopTypingJson, senderSession);
        } catch (Exception e) {
            System.err.println("Error manejando stop typing: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ✅ Métodos auxiliares con validación
    private String getSalaId(WebSocketSession session) {
        Object salaId = session.getAttributes().get("salaId");
        return salaId != null ? salaId.toString() : null;
    }

    private Long getUsuarioId(WebSocketSession session) {
        Object usuarioId = session.getAttributes().get("usuarioId");
        if (usuarioId instanceof Long) {
            return (Long) usuarioId;
        } else if (usuarioId instanceof String) {
            try {
                return Long.parseLong((String) usuarioId);
            } catch (NumberFormatException e) {
                System.err.println("Error convirtiendo usuarioId a Long: " + usuarioId);
                return null;
            }
        }
        return null;
    }

    private void broadcastToSala(String salaId, String mensaje) {
        List<WebSocketSession> sesiones = sesionesPorSala.get(salaId);
        if (sesiones != null) {
            sesiones.removeIf(session -> !session.isOpen());

            for (WebSocketSession session : sesiones) {
                try {
                    session.sendMessage(new TextMessage(mensaje));
                } catch (Exception e) {
                    System.err.println("Error enviando mensaje a sesión: " + e.getMessage());
                }
            }
        }
    }

    private void broadcastToSalaExcepto(String salaId, String mensaje, WebSocketSession excluir) {
        List<WebSocketSession> sesiones = sesionesPorSala.get(salaId);
        if (sesiones != null) {
            sesiones.removeIf(session -> !session.isOpen());

            for (WebSocketSession session : sesiones) {
                if (!session.equals(excluir)) {
                    try {
                        session.sendMessage(new TextMessage(mensaje));
                    } catch (Exception e) {
                        System.err.println("Error enviando mensaje a sesión: " + e.getMessage());
                    }
                }
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        String salaId = getSalaId(session);
        List<WebSocketSession> sesiones = sesionesPorSala.get(salaId);

        if (sesiones != null) {
            sesiones.remove(session);
            if (sesiones.isEmpty()) {
                sesionesPorSala.remove(salaId);
            }
        }

        System.out.println("Usuario desconectado del chat de sala: " + salaId);
    }

    // Cuando se elimina un mensaje
    private void manejarEliminarMensaje(String salaId, Long usuarioId, JsonNode json) {
        try {
            Long mensajeId = json.get("mensajeId").asLong();
            chatService.eliminarMensaje(mensajeId, usuarioId);

            // Broadcast a todos los usuarios de la sala
            String mensajeJson = objectMapper.writeValueAsString(Map.of(
                    "type", "delete-message",
                    "mensajeId", mensajeId));
                    System.out.println("🧨 Notificando eliminación mensajeId = " + mensajeId + " a sala = " + salaId);

            broadcastToSala(salaId, mensajeJson);

        } catch (Exception e) {
            System.err.println("Error eliminando mensaje de chat: " + e.getMessage());
        }
    }

    // Cuando se edita un mensaje
    private void manejarEditarMensaje(String salaId, Long usuarioId, JsonNode json) {
        try {
            Long mensajeId = json.get("mensajeId").asLong();
            String nuevoContenido = json.get("contenido").asText();
            MensajeChatDTO mensajeEditado = chatService.editarMensaje(mensajeId, nuevoContenido, usuarioId);

            // Broadcast a todos los usuarios de la sala
            String mensajeJson = objectMapper.writeValueAsString(Map.of(
                    "type", "edit-message",
                    "mensaje", mensajeEditado));
            broadcastToSala(salaId, mensajeJson);

        } catch (Exception e) {
            System.err.println("Error editando mensaje de chat: " + e.getMessage());
        }
    }

}
