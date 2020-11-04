package com.wannistudio.talk.service;

import com.wannistudio.talk.domain.Member;
import com.wannistudio.talk.repository.abstracts.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
@Rollback
class MemberServiceTest {
    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;

    @Test
    void joinTest() {
        // given
        Member member = new Member();
        member.setEmail("chlgudwnd123@gmail.com");
        member.setName("choi");
        member.setPassword("1324");

        // when
        Long savedId = memberService.join(member);

        //then
        assertEquals(member, memberRepository.findOne(savedId).orElse(null));
    }

    @Test
    void checkDuplicate() {
        // given
        Member member1 = new Member();
        member1.setEmail("chlgudwnd123@gmail.com");
        member1.setName("choi");
        member1.setPassword("1324");
        // given
        Member member2 = new Member();
        member2.setEmail("chlgudwnd123@gmail.com");
        member2.setName("choi");
        member2.setPassword("1324");

        assertThrows(IllegalStateException.class, () -> {
            memberService.join(member1);
            memberService.join(member2); //Exception!!
        });
    }
}