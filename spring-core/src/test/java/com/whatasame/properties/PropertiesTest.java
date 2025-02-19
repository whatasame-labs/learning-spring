package com.whatasame.properties;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

@SpringBootTest
@DisplayName("학습 테스트: Properties")
class PropertiesTest {

    @Autowired
    ApplicationContext applicationContext;

    @Test
    @DisplayName("생성자가 하나인 클래스는 `@ConstructorBinding`을 생략하여 사용할 수 있다.")
    void immutablePropertiesWithUniqueConstructor() {
        /* given */
        final ImmutableProperties immutableProperties = applicationContext.getBean(ImmutableProperties.class);

        /* when & then */
        assertAll(
                () -> assertThat(immutableProperties.getUsername()).isEqualTo("shine"),
                () -> assertThat(immutableProperties.getPassword()).isEqualTo("!q@w#e$r"));
    }

    @Test
    @DisplayName("Record를 이용해서 `@ConfigurationProperties` 클래스를 정의할 수 있다.")
    void recordProperties() {
        /* given */
        final RecordProperties recordProperties = applicationContext.getBean(RecordProperties.class);

        /* when & then */
        assertAll(
                () -> assertThat(recordProperties.username()).isEqualTo("novel"),
                () -> assertThat(recordProperties.password()).isEqualTo("1q2w3e4r"));
    }

    @Test
    @DisplayName("Nested properites도 정의할 수 있다.")
    void nestedProperties() {
        /* given */
        final NestedProperties nestedProperties = applicationContext.getBean(NestedProperties.class);

        /* when & then */
        assertAll(
                () -> assertThat(nestedProperties.name()).isEqualTo("sirus"),
                () -> assertThat(nestedProperties.phone()).isEqualTo("1234567890"),
                () -> assertThat(nestedProperties.city()).isEqualTo("Eye of the Storm"),
                () -> assertThat(nestedProperties.zip()).isEqualTo("36472"));
    }
}
