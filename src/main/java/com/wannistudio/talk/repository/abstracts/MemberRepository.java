package com.wannistudio.talk.repository.abstracts;

import com.wannistudio.talk.domain.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {
    void save(Member member);
    Optional<Member> findOne(Long id);
    Optional<List<Member>> findAll();
    Optional<Member> findByName(String name);
}
