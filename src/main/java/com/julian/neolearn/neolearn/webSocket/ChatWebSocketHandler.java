package com.julian.neolearn.neolearn.webSocket;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String salaId = getSalaId(session);
        sesionesPorSala.putIfAbsent(salaId, new ArrayList<>());
        sesionesPorSala.get(salaId).add(session);
        
        System.out.println("Usuario conectado al chat de sala: " + salaId);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String salaId = getSalaId(session);
        Long usuarioId = getUsuarioId(session);
        
        try {
            JsonNode json = objectMapper.readTree(message.getPayload());
            String tipo = json.get("type").asText();
            
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
            }
        } catch (Exception e) {
            System.err.println("Error procesando mensaje de chat: " + e.getMessage());
        }
    }

    private void manejarMensajeChat(String salaId, Long usuarioId, JsonNode json) {
        try {
            String contenido = json.get("contenido").asText();
            Long claseId = json.get("claseId").asLong();
            
            EnviarMensajeRequest request = new EnviarMensajeRequest();
            request.setContenido(contenido);
            
            MensajeChatDTO mensajeDTO = chatService.enviarMensaje(claseId, usuarioId, request);
            
            // Broadcast del mensaje a todos en la sala
            String mensajeJson = objectMapper.writeValueAsString(Map.of(
                "type", "new-message",
                "mensaje", mensajeDTO
            ));
            
            broadcastToSala(salaId, mensajeJson);
            
        } catch (Exception e) {
            System.err.println("Error enviando mensaje de chat: " + e.getMessage());
        }
    }

    private void manejarTyping(String salaId, Long usuarioId, WebSocketSession senderSession) {
        try {
            String typingJson = objectMapper.writeValueAsString(Map.of(
                "type", "user-typing",
                "usuarioId", usuarioId
            ));
            
            broadcastToSalaExcepto(salaId, typingJson, senderSession);
        } catch (Exception e) {
            System.err.println("Error manejando typing: " + e.getMessage());
        }
    }

    private void manejarStopTyping(String salaId, Long usuarioId, WebSocketSession senderSession) {
        try {
            String stopTypingJson = objectMapper.writeValueAsString(Map.of(
                "type", "user-stop-typing",
                "usuarioId", usuarioId
            ));
            
            broadcastToSalaExcepto(salaId, stopTypingJson, senderSession);
        } catch (Exception e) {
            System.err.println("Error manejando stop typing: " + e.getMessage());
        }
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

    private String getSalaId(WebSocketSession session) {
        return (String) session.getAttributes().get("salaId");
    }

    private Long getUsuarioId(WebSocketSession session) {
        return (Long) session.getAttributes().get("usuarioId");
    }
}
