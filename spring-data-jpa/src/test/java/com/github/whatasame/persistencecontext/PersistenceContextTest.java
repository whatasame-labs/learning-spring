package com.github.whatasame.persistencecontext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.assertAll;

import jakarta.persistence.TransactionRequiredException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@DisplayName("학습 테스트: Persistence context")
@SpringBootTest
public class PersistenceContextTest {

    @Autowired
    TransactionPersistenceContext transactionContext;

    @Autowired
    ExtendedPersistenceContext extendedContext;

    @Nested
    @DisplayName("Transaction type은")
    class TransactionType {

        @Test
        @DisplayName("Transaction 종료 시 persistence context에 있는 entity를 persistence storage에 flush한다.")
        void flushPersistenceContextToStorage() {
            final Member member = new Member("whatasame");

            transactionContext.saveWithTransaction(member); // flush and commit

            assertThat(transactionContext.find(member.getId())).isNotNull(); // not exists in context so query sql
        }

        @Test
        @DisplayName("Transaction 없이 entity를 조작할 수 없다.")
        void withoutTransaction() {
            final Member member = new Member("whatasame");

            assertThatCode(() -> transactionContext.saveWithoutTransaction(member))
                    .isExactlyInstanceOf(TransactionRequiredException.class)
                    .hasMessageContaining("No EntityManager with actual transaction available for current thread");
        }
    }

    @Nested
    @DisplayName("Extended type은")
    class ExtendedType {

        @Test
        @DisplayName("Transaction 종료 시 persistence context에 있는 entity를 persistence storage에 flush한다.")
        void notFlushPersistenceContextToStorage() {
            final Member member = new Member("whatasame");

            extendedContext.saveWithTransaction(member); // flush and commit

            assertThat(extendedContext.find(member.getId())).isNotNull();
        }

        @Test
        @DisplayName("Transaction 없이 entity를 조작할 수 있지만 persistence storage에 저장되진 않는다.")
        void withoutTransaction() {
            final Member member = new Member("whatasame");

            extendedContext.saveWithoutTransaction(member);

            assertAll(
                    () -> assertThat(extendedContext.find(member.getId()))
                            .isNotNull(), // exists in context so does not query sql
                    () -> assertThat(transactionContext.find(member.getId()))
                            .isNull()); // but other context does not know so query sql
        }
    }
}
