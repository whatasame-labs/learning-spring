package com.github.whatasame.beannaming;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NoUniqueBeanDefinitionException;
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
    @DisplayName("Bean의 이름을 지정하지 않으면 메서드 이름을 사용한다.")
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

    @Test
    @DisplayName("동일한 클래스의 빈이 여러개 존재할 경우, 빈 이름으로 조회해야한다.")
    void findBeanByNameInDuplicatedClassBean() {
        /* given */

        /* when */

        /* then */
        assertAll(
                () ->
                        assertThatCode(() -> applicationContext.getBean(Animal.class))
                                .isExactlyInstanceOf(NoUniqueBeanDefinitionException.class),
                () ->
                        assertThat(applicationContext.getBean("dog"))
                                .isNotNull()
                                .isExactlyInstanceOf(Dog.class),
                () ->
                        assertThat(applicationContext.getBean("cat"))
                                .isNotNull()
                                .isExactlyInstanceOf(Cat.class));
    }

    @Test
    @DisplayName("동일한 클래스의 빈이 여러개 존재할 경우, @Qualifier로 구분하여 주입받아야한다.")
    void injectBeanByQualifierInDuplicatedClassBean() {
        /* given */

        /* when */

        /* then */
        assertThat(applicationContext.getBean(Circus.class))
                .isNotNull()
                .isExactlyInstanceOf(Circus.class);
    }
}
