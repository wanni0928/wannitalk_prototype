package com.wannistudio.talk.handler;

import lombok.SneakyThrows;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.*;

@Component
public class SocketHandler extends TextWebSocketHandler {
    List<Map<String, Object>> roomListSessions = new ArrayList<>();
//    Map<String, WebSocketSession> sessionMap = new HashMap<>(); // 웹 소켓 세션을 담을 맵


    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        //메시지 발송
        String msg = message.getPayload();
        JSONObject obj = jsonToObjectParser(msg);

        String compareRoomNumber = (String) obj.get("roomNumber");
        Map<String, Object> temp = new HashMap<>();
        if(roomListSessions.size() > 0) {
            // 세션 리스트의 저장된 방 번호를 가져와서
            for (Map<String, Object> roomListSession : roomListSessions) {
                String roomNumber = (String) roomListSession.get("roomNumber");
                if (roomNumber.equals(compareRoomNumber)) {
                    // 같은 방이 존재 한다면, 해당 반 번호의 세션 리스트의 존재하는 모든 object 값을 가져온다.
                    temp = roomListSession;
                    break;
                }
            }

            // 해당 방의 세션들만 찾아서 메시지를 발송해준다.
            for (String key : temp.keySet()) {
                if(key.equals("roomNumber")){
                    continue;
                }

                WebSocketSession webSocketSession = (WebSocketSession) temp.get(key);
                Optional.of(webSocketSession).ifPresent(wss -> {
                    try {
                        wss.sendMessage(new TextMessage(obj.toJSONString()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        //소켓 연결
        super.afterConnectionEstablished(session);
        boolean flag = false;
        String url = session.getUri().toString();
        String roomNumber = url.split("/chatting/")[1];
        int idx = roomListSessions.size(); //방의 사이즈를 조사한다.
        if(roomListSessions.size() > 0) {
            for(int i=0; i<roomListSessions.size(); i++) {
                String rN = (String) roomListSessions.get(i).get("roomNumber");
                if(rN.equals(roomNumber)) {
                    flag = true;
                    idx = i;
                    break;
                }
            }
        }

        if(flag) { //존재하는 방이라면 세션만 추가한다.
            Map<String, Object> map = roomListSessions.get(idx);
            map.put(session.getId(), session);
        }else { //최초 생성하는 방이라면 방번호와 세션을 추가한다.
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("roomNumber", roomNumber);
            map.put(session.getId(), session);
            roomListSessions.add(map);
        }

        JSONObject obj = new JSONObject();
        obj.put("type", "getId");
        obj.put("sessionId", session.getId());
        session.sendMessage(new TextMessage(obj.toJSONString()));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        //소켓 종료
        if(roomListSessions.size() > 0) { //소켓이 종료되면 해당 세션값들을 찾아서 지운다.
            for(int i=0; i<roomListSessions.size(); i++) {
                roomListSessions.get(i).remove(session.getId());
            }
        }
        super.afterConnectionClosed(session, status);
    }

    @SneakyThrows
    private static JSONObject jsonToObjectParser(String jsonStr) {
        JSONParser parser = new JSONParser();
        return (JSONObject) parser.parse(jsonStr);
    }
}
