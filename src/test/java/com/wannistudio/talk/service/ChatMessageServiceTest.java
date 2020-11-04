package com.wannistudio.talk.service;

import com.wannistudio.talk.domain.ChatMessage;
import com.wannistudio.talk.domain.Member;
import com.wannistudio.talk.repository.abstracts.ChatMessageRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@SpringBootTest
@Transactional
@Rollback
class ChatMessageServiceTest {
    @Autowired
    ChatMessageService chatMessageService;
    @Autowired
    ChatMessageRepository chatMessageRepository;
    @Autowired
    MemberService memberService;

    @Test
    void sendMsg() {
        // given
        Member member = new Member();
        member.setEmail("chlgudwnd123@gmail.com");
        member.setName("choi");
        member.setPassword("1234");
        Long memberId = memberService.join(member);

        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setMember(member);
        chatMessage.setMessage("하이");
        chatMessage.setTime(LocalDateTime.now());

        // when
        Long msgId = chatMessageService.sendMsg(chatMessage);
        ChatMessage cmsg = chatMessageRepository.findOne(msgId).orElse(null);

        // then
        Assertions.assertEquals(cmsg.getMember().getId(), memberId);
    }

    @Test
    void findMsg() {
    }
}