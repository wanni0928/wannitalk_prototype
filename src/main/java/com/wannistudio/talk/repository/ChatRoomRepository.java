package com.wannistudio.talk.repository;

import com.wannistudio.talk.domain.ChatRoom;

import java.util.List;

public interface ChatRoomRepository {
    List<ChatRoom> findAllRoom();
    ChatRoom findRoomById(String id);
    ChatRoom createChatRoom(String name);
}
