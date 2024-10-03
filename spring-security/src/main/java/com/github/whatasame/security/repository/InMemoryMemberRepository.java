package com.github.whatasame.security.repository;

import com.github.whatasame.security.model.Member;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryMemberRepository {

    private final Map<Long, Member> members = Map.of(
            1L, new Member(1L, "test@example.com", "password123", List.of("ROLE_USER")),
            2L, new Member(2L, "sample@example.com", "password123", List.of("ROLE_ADMIN")),
            3L, new Member(3L, "another@example.com", "password123", List.of("ROLE_USER")));

    public Optional<Member> findById(final long memberId) {
        return Optional.ofNullable(members.get(memberId));
    }

    public Optional<Member> findByEmail(final String email) {
        return members.values().stream()
                .filter(member -> member.email().equals(email))
                .findFirst();
    }
}
