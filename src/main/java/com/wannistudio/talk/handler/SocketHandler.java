package com.wannistudio.talk.handler;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.HashMap;
import java.util.Map;

@Component
public class SocketHandler extends TextWebSocketHandler {
    Map<String, WebSocketSession> sessionMap = new HashMap<>(); // 웹 소켓 세션을 담을 맵

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // 메세지 발송
        String msg = message.getPayload();
        for (String key : sessionMap.keySet()) {
            WebSocketSession wss = sessionMap.get(key);
            wss.sendMessage(new TextMessage(msg));
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // 소켓 연결
        super.afterConnectionEstablished(session);
        sessionMap.put(session.getId(), session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        // 소켓 종료
        sessionMap.remove(session.getId());
        super.afterConnectionClosed(session, status);
    }
}
