package com.wannistudio.talk.repository.abstracts;

import com.wannistudio.talk.domain.ChatMessage;
import com.wannistudio.talk.domain.ChatMessageSearch;

import java.util.List;
import java.util.Optional;

public interface ChatMessageRepository {
    void save(ChatMessage chatMessage);
    Optional<ChatMessage> findOne(Long id);
    Optional<List<ChatMessage>> findAll();
    Optional<List<ChatMessage>> findAllByName(ChatMessageSearch search);
}
