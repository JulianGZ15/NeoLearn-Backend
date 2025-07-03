package com.julian.neolearn.neolearn.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import com.julian.neolearn.neolearn.webSocket.WebSocketHandshakeInterceptor;
import com.julian.neolearn.neolearn.webSocket.WebSocketSignalingHandler;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new WebSocketSignalingHandler(), "/ws/sala/{codigoSala}")
                .addInterceptors(new WebSocketHandshakeInterceptor())
                .setAllowedOrigins("http://localhost:4200", "https://qjsfhr3w-4200.use2.devtunnels.ms/") // Más específico para producción
                ; // Agregar SockJS como fallback
    }
}

