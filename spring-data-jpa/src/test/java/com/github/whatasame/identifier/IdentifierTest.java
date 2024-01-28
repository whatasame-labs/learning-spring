package com.github.whatasame.identifier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DisplayName("학습 테스트: Hibernate identifier")
@DataJpaTest
class IdentifierTest {

    @Autowired EntityManager entityManager;

    @AfterEach
    void tearDown() {
        entityManager.clear();
    }

    @Test
    @DisplayName("기본 키를 직접 할당한다.")
    void persistAssignedIdentifier() {
        /* given */
        final AssignedIdentifierEntity entity = new AssignedIdentifierEntity();
        entity.setId(777L);

        /* when */
        entityManager.persist(entity);
        entityManager.flush();

        /* then */
        assertThat(entityManager.find(AssignedIdentifierEntity.class, 777L)).isNotNull();
    }

    @Test
    @DisplayName("@GeneratedValu Entity는 기본 키를 persist 시 generation strategy에 따라 자동 생성한다.")
    void persistGeneratedIdentifier() {
        /* given */
        final GeneratedIdentifierEntity entity = new GeneratedIdentifierEntity();

        /* when */
        entityManager.persist(entity);
        entityManager.flush();

        /* then */
        assertThat(entity.getId()).isNotNull();
    }

    @Test
    @DisplayName("@GeneratedValue Entity에 기본 값을 직접 지정하고 persist 시 EntityExistsException이 발생한다.")
    void persistGeneratedIdentifierWithAssignedValue() {
        /* given */
        final GeneratedIdentifierEntity entity = new GeneratedIdentifierEntity();
        entity.setId(777L);

        /* when & then */
        assertThatCode(() -> entityManager.persist(entity))
                .isExactlyInstanceOf(EntityExistsException.class);
    }

    @Test
    @DisplayName("@GeneratedValue Entity에 기본 값을 직접 지정하고 merge 시 generation strategy에 따라 자동 생성한다.")
    void mergeGeneratedIdentifierWithAssignedValue() {
        /* given */
        final GeneratedIdentifierEntity entity = new GeneratedIdentifierEntity();
        entity.setId(777L);

        /* when */
        entityManager.merge(entity);
        entityManager.flush();

        /* then */
        assertThat(entity.getId()).isNotNull().isNotEqualTo(777L);
    }
}
