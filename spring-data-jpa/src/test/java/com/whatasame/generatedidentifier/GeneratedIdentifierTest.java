package com.whatasame.generatedidentifier;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DisplayName("학습 테스트: Hibernate generated identifier")
@DataJpaTest
class GeneratedIdentifierTest {

    @Autowired
    EntityManager entityManager;

    @Nested
    @DisplayName("자동 키 생성 전략이 AUTO일 때")
    class AutoStrategy {

        @Test
        @DisplayName("기본 키가 UUID면 UUID generator를 사용한다.")
        void uuid() {
            final AutoStrategyUuidEntity entity = new AutoStrategyUuidEntity();

            entityManager.persist(entity);
            entityManager.flush();

            assertThat(entity.getId()).isNotNull();
        }

        @Test
        @DisplayName("기본 키가 Numeric이면 SequenceStyleGenerator를 사용한다.")
        void numeric() {
            final AutoStrategyNumericEntity entity = new AutoStrategyNumericEntity();

            entityManager.persist(entity);
            entityManager.flush();

            assertThat(entity.getId()).isNotNull();
        }
    }

    @Nested
    @DisplayName("자동 키 생성 전략이 SEQUENCE일 때")
    class SequenceStrategy {

        @Test
        @DisplayName("SequenceStyleGenerator를 사용하여 데이터베이스에 관계없이 시퀀스로 기본 키를 생성한다.")
        void sequence() {
            final SequenceStrategyEntity entity = new SequenceStrategyEntity();

            entityManager.persist(entity);
            entityManager.flush();

            assertThat(entity.getId()).isNotNull();
        }
    }

    @Nested
    @DisplayName("자동 키 생성 전략이 IDENTITY일 때")
    class IdentityStrategy {

        @Test
        @DisplayName("IdentityGenerator를 사용하여 데이터베이스에 의존한 기본 키를 생성한다.")
        void identity() {
            final IdentityStrategyEntity entity = new IdentityStrategyEntity();

            entityManager.persist(entity); // ID 값을 알기 위해 INSERT를 수행해야하므로 flush 하지 않아도 insert 쿼리가 실행된다.

            assertThat(entity.getId()).isNotNull();
        }
    }

    @Nested
    @DisplayName("자동 키 생성 전략이 TABLE일 때")
    class TableStrategy {

        @Test
        @DisplayName("데이터베이스에 의존하여 기본 키를 생성한다.")
        void table() {
            final TableStrategyEntity entity = new TableStrategyEntity();

            entityManager.persist(entity);
            entityManager.flush();

            assertThat(entity.getId()).isNotNull();
        }
    }
}
