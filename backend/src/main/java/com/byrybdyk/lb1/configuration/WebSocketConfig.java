//package com.byrybdyk.lb1.configuration;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.messaging.MessageChannel;
//import org.springframework.messaging.simp.SimpMessagingTemplate;
//import org.springframework.messaging.simp.config.MessageBrokerRegistry;
//import org.springframework.messaging.simp.stomp.StompProtocolHandler;
//import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
//import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
//import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
//import org.springframework.integration.channel.DirectChannel;
//
//@Configuration
//@EnableWebSocketMessageBroker
//public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
//
//    @Override
//    public void configureMessageBroker(MessageBrokerRegistry registry) {
//        registry.enableSimpleBroker("/topic");  // Simple in-memory message broker
//        registry.setApplicationDestinationPrefixes("/app");  // Prefix for app-level messages
//    }
//
//    @Override
//    public void registerStompEndpoints(StompEndpointRegistry registry) {
//        registry.addEndpoint("/ws").withSockJS();  // Endpoint for WebSocket connection
//    }
//
//    @Bean
//    public SimpMessagingTemplate simpMessagingTemplate(MessageChannel messageChannel) {
//        return new SimpMessagingTemplate(messageChannel);  // Provide MessageChannel as argument
//    }
//
//    @Bean
//    public MessageChannel messageChannel() {
//        return new DirectChannel();  // Default message channel
//    }
//}
