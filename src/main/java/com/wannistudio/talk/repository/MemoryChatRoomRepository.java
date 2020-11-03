package com.wannistudio.talk.repository;

import com.wannistudio.talk.domain.ChatRoom;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.*;

@Repository
public class MemoryChatRoomRepository implements ChatRoomRepository {
    private Map<String, ChatRoom> chatRoomMap;


    @PostConstruct
    public void init() {
        chatRoomMap = new LinkedHashMap<>();
    }

    @Override
    public List<ChatRoom> findAllRoom() {
        List<ChatRoom> chatRooms = new ArrayList<>(chatRoomMap.values());
        Collections.reverse(chatRooms);
        return chatRooms;
    }

    @Override
    public ChatRoom findRoomById(String id) {
        return chatRoomMap.get(id);
    }

    @Override
    public ChatRoom createChatRoom(String name) {
        ChatRoom chatRoom = ChatRoom.create(name);
        chatRoomMap.put(chatRoom.getRoomId(), chatRoom);
        return chatRoom;
    }
}
