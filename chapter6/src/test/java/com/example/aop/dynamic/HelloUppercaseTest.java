package com.example.aop.dynamic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class HelloUppercaseTest {

    @DisplayName("프록시 객체를 통한 부가기능 추가 테스트")
    @Test
    void testCase1() {
        Hello proxyHello = new HelloUppercase(new HelloTarget());
        assertThat(proxyHello.sayHello("SR")).isEqualTo("HELLO SR");
        assertThat(proxyHello.sayHi("SR")).isEqualTo("HI SR");
        assertThat(proxyHello.sayThankYou("SR")).isEqualTo("THANK YOU SR");
    }
}