package com.xyz.social_media.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import com.xyz.social_media.config.AuthChannelInterceptor;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // Enable a simple in-memory broker and destination prefix for the app
        config.enableSimpleBroker("/topic");
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Client will connect to /ws (SockJS fallback enabled)
        registry.addEndpoint("/ws")
                .setAllowedOrigins("http://localhost:3000", "http://social-media0282.s3-website.ap-south-1.amazonaws.com")
                .withSockJS();
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        // add interceptor to validate CONNECT headers
        // Temporarily disabled for local testing - uncomment for production
        // registration.interceptors(new AuthChannelInterceptor());
    }
}
