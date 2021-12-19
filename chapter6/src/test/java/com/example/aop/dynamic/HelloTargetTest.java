package com.example.aop.dynamic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class HelloTargetTest {

    @DisplayName("HelloTarget 오브젝트를 사용하는 클라이언트 역할을 하는 테스트")
    @Test
    void testCase1() {
        Hello hello = new HelloTarget(); // 타킷은 인터페이스를 통해 접근하는 습관 필요
        assertThat(hello.sayHello("SR")).isEqualTo("Hello SR");
        assertThat(hello.sayHi("SR")).isEqualTo("Hi SR");
        assertThat(hello.sayThankYou("SR")).isEqualTo("Thank You SR");
    }

}