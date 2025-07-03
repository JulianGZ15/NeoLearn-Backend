package com.julian.neolearn.neolearn.handler;

import java.util.Map;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;


@Component
public class ChatHandshakeInterceptor implements HandshakeInterceptor {

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        
        if (request instanceof ServletServerHttpRequest servletRequest) {
            String path = servletRequest.getServletRequest().getRequestURI();
            String salaId = path.substring(path.lastIndexOf('/') + 1);
            attributes.put("salaId", salaId);
            
            // ✅ EXTRAER usuarioId del query parameter (más simple y confiable)
            String usuarioIdParam = servletRequest.getServletRequest().getParameter("usuarioId");
            
            System.out.println("✅ Id del Usuario recibido: " + usuarioIdParam);

            if (usuarioIdParam != null && !usuarioIdParam.trim().isEmpty()) {
                try {
                    Long usuarioId = Long.parseLong(usuarioIdParam);
                    attributes.put("usuarioId", usuarioId);
                    System.out.println("✅ Usuario conectado al chat: " + usuarioId + " en sala: " + salaId);
                    return true;
                } catch (NumberFormatException e) {
                    System.err.println("❌ usuarioId inválido: " + usuarioIdParam);
                    return false;
                }
            }
            
            System.err.println("❌ No se encontró usuarioId en los parámetros");
            return false;
        }
        
        return false;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                               WebSocketHandler wsHandler, Exception exception) {
        // Implementación vacía
    }
}
