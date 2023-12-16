package com.github.whatasame.beannaming;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

@SpringBootTest
@DisplayName("학습 테스트: 빈 네이밍 테스트")
class BeanNamingTest {

    @Autowired ApplicationContext applicationContext;

    @Test
    @DisplayName("Component의 이름을 지정하지 않으면 클래스 이름에서 첫 글자를 소문자로 바꾼 이름을 사용한다.")
    void defaultComponentNaming() {
        /* given */

        /* when */

        /* then */
        assertThat(applicationContext.getBean("defaultComponent"))
                .isNotNull()
                .isExactlyInstanceOf(DefaultComponent.class);
    }

    @Test
    @DisplayName("Component의 value 필드로 이름을 지정하면 지정한 이름을 사용한다.")
    void componentValueNaming() {
        /* given */

        /* when */

        /* then */
        assertThat(applicationContext.getBean("super-duper-component"))
                .isNotNull()
                .isExactlyInstanceOf(NamedComponent.class);
    }

    @Test
    @DisplayName("Bean의 이름을 지정하지 않으면 클래스 이름을 사용한다.")
    void defaultBeanNaming() {
        /* given */

        /* when */

        /* then */
        assertThat(applicationContext.getBean("defaultBean"))
                .isNotNull()
                .isExactlyInstanceOf(DefaultBean.class);
    }

    @Test
    @DisplayName("Bean의 value 필드로 이름을 지정하면 지정한 이름을 사용한다.")
    void beanValueNaming() {
        /* given */

        /* when */

        /* then */
        assertThat(applicationContext.getBean("super-duper-bean"))
                .isNotNull()
                .isExactlyInstanceOf(NamedBean.class);
    }
}
