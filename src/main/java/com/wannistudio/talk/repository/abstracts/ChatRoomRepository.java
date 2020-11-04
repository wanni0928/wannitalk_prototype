package com.wannistudio.talk.repository.abstracts;

import com.wannistudio.talk.domain.ChatRoom;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository {
    void save(ChatRoom chatRoom);
    List<ChatRoom> findAllRoom();
    Optional<ChatRoom> findRoomById(Long id);
//    Optional<ChatRoom> createChatRoom(String name);
}
