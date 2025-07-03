package com.julian.neolearn.neolearn.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import com.julian.neolearn.neolearn.handler.ChatHandshakeInterceptor;
import com.julian.neolearn.neolearn.handler.ChatWebSocketHandler;
import com.julian.neolearn.neolearn.service.MensajeChatService;

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
                .setAllowedOrigins("http://localhost:4200", "https://qjsfhr3w-4200.use2.devtunnels.ms/");
    }

   
}
