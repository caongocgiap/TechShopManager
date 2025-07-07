package com.techshop.server.infrastructure.config.websocket.service;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class WebSocketService {

    SimpMessagingTemplate template;

    public WebSocketService(SimpMessagingTemplate template) {
        this.template = template;
    }

    public void sendMessage(String topic, Object message) {
        template.convertAndSend(topic, message);
    }

    public void broadcastMessage(Object message) {
        template.convertAndSend("/topic/message", message);
    }

    public void receiveMessage(Object message) {
        System.out.println(message);
    }

}
