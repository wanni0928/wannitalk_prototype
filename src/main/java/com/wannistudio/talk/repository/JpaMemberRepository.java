package com.wannistudio.talk.repository;

import com.wannistudio.talk.domain.Member;
import com.wannistudio.talk.repository.abstracts.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JpaMemberRepository implements MemberRepository {

    private final EntityManager em;

    @Override
    public void save(Member member) {
        em.persist(member);
    }

    @Override
    public Optional<Member> findOne(Long id) {
        return Optional.ofNullable(em.find(Member.class, id));
    }

    @Override
    public Optional<List<Member>> findAll() {
        return Optional.ofNullable(em.createQuery("select m from Member m", Member.class).getResultList());
    }

    @Override
    public Optional<Member> findByName(String name) {
        List<Member> resultList = em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", name)
                .getResultList();
        return resultList.stream().findAny();
    }
}
