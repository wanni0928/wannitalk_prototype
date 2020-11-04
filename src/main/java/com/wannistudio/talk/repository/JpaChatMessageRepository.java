package com.wannistudio.talk.repository;

import com.wannistudio.talk.domain.ChatMessage;
import com.wannistudio.talk.domain.ChatMessageSearch;
import com.wannistudio.talk.repository.abstracts.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JpaChatMessageRepository implements ChatMessageRepository {
    private final EntityManager em;

    @Override
    public void save(ChatMessage chatMessage) {
        em.persist(chatMessage);
    }

    @Override
    public Optional<ChatMessage> findOne(Long id) {
        return Optional.ofNullable(em.find(ChatMessage.class, id));
    }

    @Override
    public Optional<List<ChatMessage>> findAll() {
        return Optional.ofNullable(em.createQuery("select cm from ChatMessage cm", ChatMessage.class).getResultList());
    }

    @Override
    public Optional<List<ChatMessage>> findAllByName(ChatMessageSearch search) {
        return Optional.ofNullable(em.createQuery("select cm from ChatMessage cm join cm.member m where m.name like :name", ChatMessage.class)
                .setParameter("name", search.getMemberName())
                .setMaxResults(1000)
                .getResultList());
    }
}
