package com.wannistudio.talk.repository;

import com.wannistudio.talk.domain.ChatRoom;
import com.wannistudio.talk.repository.abstracts.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JpaChatRoomRepository implements ChatRoomRepository {

    private final EntityManager em;

    @Override
    public void save(ChatRoom chatRoom) {
        em.persist(chatRoom);
    }

    @Override
    public List<ChatRoom> findAllRoom() {
        return em.createQuery("select c from ChatRoom c", ChatRoom.class).getResultList();
    }

    @Override
    public Optional<ChatRoom> findRoomById(Long id) {
        return Optional.ofNullable(em.find(ChatRoom.class, id));
    }
}
