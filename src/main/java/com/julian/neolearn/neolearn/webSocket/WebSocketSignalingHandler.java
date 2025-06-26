package com.julian.neolearn.neolearn.webSocket;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class WebSocketSignalingHandler extends TextWebSocketHandler {

    private final Map<String, List<WebSocketSession>> sesionesPorSala = new ConcurrentHashMap<>();
    private final Map<String, Set<String>> participantesPorSala = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String salaId = getCodigoSala(session);
        sesionesPorSala.putIfAbsent(salaId, new ArrayList<>());
        participantesPorSala.putIfAbsent(salaId, new HashSet<>());

        sesionesPorSala.get(salaId).add(session);

        System.out.println("Nueva conexión establecida en sala: " + salaId);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String salaId = getCodigoSala(session);
        List<WebSocketSession> sesiones = sesionesPorSala.get(salaId);

        if (sesiones == null) {
            System.err.println("Sala no encontrada: " + salaId);
            return;
        }

        try {
            String payload = message.getPayload();
            ObjectMapper mapper = new ObjectMapper();
            JsonNode json = mapper.readTree(payload);

            String type = json.get("type").asText();
            String senderId = json.get("senderId").asText();
            String targetId = json.has("targetId") ? json.get("targetId").asText() : null;

            // Guardar senderId en la sesión
            session.getAttributes().put("senderId", senderId);

             System.out.println("Mensaje recibido - Tipo: " + type + 
                      ", Sender: " + senderId + 
                      ", Target: " + (targetId != null ? targetId : "null") +
                      ", Total sesiones en sala: " + sesiones.size());
    
    if ("join".equals(type)) {
        manejarUsuarioUnido(salaId, senderId, sesiones, session);
        return;
    }

            // Reenvío de mensajes WebRTC
            reenviarMensaje(sesiones, session, payload, targetId);

        } catch (Exception e) {
            System.err.println("Error procesando mensaje: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        String salaId = getCodigoSala(session);
        String senderId = (String) session.getAttributes().get("senderId");

        // Remover sesión
        List<WebSocketSession> sesiones = sesionesPorSala.get(salaId);
        if (sesiones != null) {
            sesiones.remove(session);

            // Limpiar sala vacía
            if (sesiones.isEmpty()) {
                sesionesPorSala.remove(salaId);
                participantesPorSala.remove(salaId);
                System.out.println("Sala eliminada por estar vacía: " + salaId);
            }
        }

        // Notificar a otros participantes
        if (senderId != null) {
            notificarUsuarioDesconectado(salaId, senderId);
        }

        System.out.println("Usuario desconectado: " + senderId + " de sala: " + salaId);
    }

    // WebSocketSignalingHandler.java
    private void manejarUsuarioUnido(String salaId, String senderId, List<WebSocketSession> sesiones,
            WebSocketSession newSession) {
        // Agregar a la lista de participantes
        participantesPorSala.get(salaId).add(senderId);

        // ✅ Estructura consistente del mensaje
        String notificacion = String.format(
                "{\"type\":\"user-joined\",\"senderId\":\"%s\",\"userId\":\"%s\",\"data\":{\"participantes\":%d}}",
                senderId,
                senderId,
                participantesPorSala.get(salaId).size());

        for (WebSocketSession s : sesiones) {
            if (!s.equals(newSession) && s.isOpen()) {
                try {
                    s.sendMessage(new TextMessage(notificacion));
                } catch (Exception e) {
                    System.err.println("Error enviando notificación de usuario unido: " + e.getMessage());
                }
            }
        }

        // Enviar lista de participantes existentes al nuevo usuario
        try {
            String participantesJson = new ObjectMapper().writeValueAsString(participantesPorSala.get(salaId).stream()
                    .filter(p -> !p.equals(senderId))
                    .collect(Collectors.toList()));
            String participantesExistentes = String.format(
                    "{\"type\":\"existing-participants\",\"senderId\":\"%s\",\"data\":{\"participants\":%s}}",
                    senderId,
                    participantesJson);

            newSession.sendMessage(new TextMessage(participantesExistentes));
        } catch (Exception e) {
            System.err.println("Error enviando participantes existentes: " + e.getMessage());
        }
    }

    private void notificarUsuarioDesconectado(String salaId, String senderId) {
        Set<String> participantes = participantesPorSala.get(salaId);
        if (participantes != null) {
            participantes.remove(senderId);
        }

        List<WebSocketSession> sesiones = sesionesPorSala.get(salaId);
        if (sesiones != null) {
            // ✅ Estructura consistente del mensaje
            String notificacion = String.format(
                    "{\"type\":\"user-left\",\"senderId\":\"%s\",\"userId\":\"%s\",\"data\":{\"participantes\":%d}}",
                    senderId,
                    senderId,
                    participantes != null ? participantes.size() : 0);

            for (WebSocketSession s : sesiones) {
                if (s.isOpen()) {
                    try {
                        s.sendMessage(new TextMessage(notificacion));
                    } catch (Exception e) {
                        System.err.println("Error notificando desconexión: " + e.getMessage());
                    }
                }
            }
        }
    }

    private void reenviarMensaje(List<WebSocketSession> sesiones, WebSocketSession sender, String payload,
            String targetId) {
        for (WebSocketSession s : sesiones) {
            if (!s.equals(sender) && s.isOpen()) {
                String receiverId = (String) s.getAttributes().get("senderId");

                if (targetId == null || (receiverId != null && receiverId.equals(targetId))) {
                    try {
                        s.sendMessage(new TextMessage(payload));
                    } catch (Exception e) {
                        System.err.println("Error reenviando mensaje: " + e.getMessage());
                    }
                }
            }
        }
    }

    private String getCodigoSala(WebSocketSession session) {
        return (String) session.getAttributes().get("codigoSala");
    }

    // Método para obtener estadísticas (opcional, para debugging)
    public Map<String, Integer> getEstadisticasSalas() {
        return sesionesPorSala.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue().size()));
    }
}
