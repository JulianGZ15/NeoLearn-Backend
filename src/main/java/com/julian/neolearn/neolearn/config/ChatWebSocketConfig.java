package com.julian.neolearn.neolearn.config;

import java.util.Map;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.HandshakeInterceptor;

import com.julian.neolearn.neolearn.service.MensajeChatService;
import com.julian.neolearn.neolearn.webSocket.ChatWebSocketHandler;

@Configuration
@EnableWebSocket
public class ChatWebSocketConfig implements WebSocketConfigurer {

    private final MensajeChatService chatService;

    public ChatWebSocketConfig(MensajeChatService chatService) {
        this.chatService = chatService;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new ChatWebSocketHandler(chatService), "/ws/chat/{salaId}")
                .addInterceptors(new ChatHandshakeInterceptor())
                .setAllowedOrigins("http://localhost:4200");
    }

    @Component
    public class ChatHandshakeInterceptor implements HandshakeInterceptor {

        @Override
        public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {

            if (request instanceof ServletServerHttpRequest servletRequest) {
                String path = servletRequest.getServletRequest().getRequestURI();
                String salaId = path.substring(path.lastIndexOf('/') + 1);
                attributes.put("salaId", salaId);

                // Extraer usuario del JWT (implementar según tu sistema de auth)
                String token = extractTokenFromRequest(servletRequest);
                Long usuarioId = extractUserIdFromToken(token);
                attributes.put("usuarioId", usuarioId);
            }

            return true;
        }

        @Override
        public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                WebSocketHandler wsHandler, Exception exception) {
        }

        private String extractTokenFromRequest(ServletServerHttpRequest request) {
            // Implementar extracción de token JWT
            return null;
        }

        private Long extractUserIdFromToken(String token) {
            // Implementar extracción de ID de usuario del token
            return null;
        }
    }
}
