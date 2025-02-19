package com.whatasame.aop;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@DisplayName("학습 테스트: AOP")
class AopTest {

    @Autowired
    AopComponent component;

    @Autowired
    AopLogger logger;

    @AfterEach
    void tearDown() {
        logger.clear();
    }

    @Test
    @DisplayName("AOP가 적용되는 Pointcut 전에 로그를 남긴다.")
    void loggingBeforeAfter() {
        /* given */

        /* when */
        component.doBefore();

        /* then */
        assertThat(logger.getLogs()).containsExactly("before");
    }

    @Test
    @DisplayName("AOP가 적용되는 Pointcut 전후에 로그를 남긴다.")
    void loggingAround() {
        /* given */

        /* when */
        component.doAround();

        /* then */
        assertThat(logger.getLogs()).containsExactly("around before", "around after");
    }

    @Test
    @DisplayName("AOP가 적용되는 Pointcut 후에 로그를 남긴다.")
    void loggingAfter() {
        /* given */

        /* when */
        component.doAfter();

        /* then */
        assertThat(logger.getLogs()).containsExactly("after");
    }
}
