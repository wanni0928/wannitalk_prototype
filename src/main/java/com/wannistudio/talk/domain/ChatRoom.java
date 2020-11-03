package com.wannistudio.talk.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter @Setter
public class ChatRoom {
    private String roomId;
    private String name;
    private Set<WebSocketSession> sessions = new HashSet<>();

    public static ChatRoom create(String name){
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.roomId = UUID.randomUUID().toString();
        chatRoom.name = name;
        return chatRoom;
    }

    public void handleMessage(WebSocketSession session, ChatMessage chatMessage, ObjectMapper objectMapper) throws IOException {
        switch (chatMessage.getType()) {
            case ENTER:
                sessions.add(session);
                chatMessage.setMessage(chatMessage.getWriter() + "님이 입장하셨습니다.");
                break;
            case LEAVE:
                sessions.remove(session);
                chatMessage.setMessage(chatMessage.getWriter() + "님임 퇴장하셨습니다.");
                break;
            default:
                chatMessage.setMessage(chatMessage.getWriter() + " : " + chatMessage.getMessage());
                break;
        }
        send(chatMessage, objectMapper);
    }

    private void send(ChatMessage chatMessage, ObjectMapper objectMapper) throws IOException {
        TextMessage textMessage = new TextMessage(objectMapper.writeValueAsString(chatMessage.getMessage()));
        for(WebSocketSession session : sessions){
            session.sendMessage(textMessage);
        }
    }
}