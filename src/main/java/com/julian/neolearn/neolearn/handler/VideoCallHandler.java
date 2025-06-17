package com.julian.neolearn.neolearn.handler;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class VideoCallHandler extends TextWebSocketHandler {
    
    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
    private final Map<String, String> roomSessions = new ConcurrentHashMap<>();
    
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.put(session.getId(), session);
        System.out.println("Conexión establecida: " + session.getId());
    }
    
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonMessage = mapper.readTree(message.getPayload());
        
        String type = jsonMessage.get("type").asText();
        String roomId = jsonMessage.has("roomId") ? jsonMessage.get("roomId").asText() : null;
        
        switch (type) {
            case "join":
                handleJoinRoom(session, roomId);
                break;
            case "offer":
            case "answer":
            case "ice-candidate":
                relayMessage(session, message, roomId);
                break;
            case "leave":
                handleLeaveRoom(session);
                break;
        }
    }
    
    private void handleJoinRoom(WebSocketSession session, String roomId) throws IOException {
        roomSessions.put(session.getId(), roomId);
        
        // Notificar a otros participantes en la sala
        sessions.values().stream()
            .filter(s -> roomId.equals(roomSessions.get(s.getId())) && !s.getId().equals(session.getId()))
            .forEach(s -> {
                try {
                    s.sendMessage(new TextMessage("{\"type\":\"user-joined\",\"userId\":\"" + session.getId() + "\"}"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
    }
    
    private void relayMessage(WebSocketSession sender, TextMessage message, String roomId) throws IOException {
        sessions.values().stream()
            .filter(s -> roomId.equals(roomSessions.get(s.getId())) && !s.getId().equals(sender.getId()))
            .forEach(s -> {
                try {
                    s.sendMessage(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
    }
    
    private void handleLeaveRoom(WebSocketSession session) throws IOException {
        String roomId = roomSessions.remove(session.getId());
        if (roomId != null) {
            sessions.values().stream()
                .filter(s -> roomId.equals(roomSessions.get(s.getId())))
                .forEach(s -> {
                    try {
                        s.sendMessage(new TextMessage("{\"type\":\"user-left\",\"userId\":\"" + session.getId() + "\"}"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
        }
    }
    
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session.getId());
        handleLeaveRoom(session);
    }
}
