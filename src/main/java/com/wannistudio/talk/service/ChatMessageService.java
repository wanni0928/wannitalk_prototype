package com.wannistudio.talk.service;

import com.wannistudio.talk.domain.ChatMessage;
import com.wannistudio.talk.repository.abstracts.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChatMessageService {
    private final ChatMessageRepository chatMessageRepository;

    // 메세지 전송
    public Long sendMsg(ChatMessage chatMessage) {
        chatMessageRepository.save(chatMessage);
        return chatMessage.getId();
    }

    // 메세지 조회
    public List<ChatMessage> findMsg() {
        return chatMessageRepository.findAll().orElse(null);
    }
}
