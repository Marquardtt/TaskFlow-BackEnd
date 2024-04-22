package br.demo.backend.websocket;

import br.demo.backend.model.Notification;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
@NoArgsConstructor
public class MyHandle extends TextWebSocketHandler {
    private ObjectMapper objectMapper;
    private
    List<WebSocketSession> sessions = new ArrayList<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
        System.out.println("SESSION " + session);
    }


    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
//        Notification notification =  objectMapper.readValue(message.toString(), Notification.class);
        System.out.println("HANDLE : " + message);
        sessions.stream().forEach( s->
        {
//            if (s.getPrincipal().getName().equals(notification.getUser().getUserDetailsEntity().getUsername())){
                try {
                    System.out.println("MESSAGE " + message);
                    s.sendMessage(message);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
//            }
        }
        );
    }
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);
    }

    public void updateNotifications(){
        sessions.stream()
                .filter(s -> s.getUri().getPath().contains("/notifications"))
                .forEach(session -> session.getPrincipal()
                );
    }


}
