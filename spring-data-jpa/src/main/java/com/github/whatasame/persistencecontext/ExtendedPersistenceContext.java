package com.github.whatasame.persistencecontext;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceContextType;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class ExtendedPersistenceContext {

    @PersistenceContext(type = PersistenceContextType.EXTENDED)
    private EntityManager entityManager;

    @Transactional
    public Member saveWithTransaction(final Member member) {
        entityManager.persist(member);

        return member;
    }

    public Member saveWithoutTransaction(final Member member) {
        entityManager.persist(member);

        return member;
    }

    public Member find(final Long id) {
        return entityManager.find(Member.class, id);
    }
}
