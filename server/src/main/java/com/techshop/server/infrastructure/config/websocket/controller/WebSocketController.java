package com.techshop.server.infrastructure.config.websocket.controller;

import com.techshop.server.infrastructure.config.websocket.request.MessageRequest;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@CrossOrigin("*")
@Hidden
public class WebSocketController {

    SimpMessagingTemplate template;

    @PostMapping("/send")
    public ResponseEntity<Void> sendMessage(@RequestBody MessageRequest textMessageDTO) {
        template.convertAndSend("/topic/message", textMessageDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @MessageMapping("/sendMessage")
    public void receiveMessage(@Payload MessageRequest textMessageDTO) {
        System.out.println(textMessageDTO.getMessage());
    }


    @SendTo("/topic/message")
    public MessageRequest broadcastMessage(@Payload MessageRequest textMessageDTO) {
        return textMessageDTO;
    }

}
