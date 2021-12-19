package com.example.aop.dynamic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Proxy;

import static org.assertj.core.api.Assertions.assertThat;

class ObjectInstanceOfUppercaseHandlerTest {

    @DisplayName("클래스 타입의 구분에 따라 부가기능을 추가하는 테스트")
    @Test
    void testCase1() {
        Hello proxyHello = (Hello) Proxy.newProxyInstance(
                getClass().getClassLoader(), // 동적으로 생성되는 다이나믹 프록시 클래스의 로딩에 사용될 클래스 로더
                new Class[]{Hello.class}, // 구현할 인터페이스
                new ObjectInstanceOfUppercaseHandler(new HelloTarget())); // 부가 기능과 위임 코드를 담은 InvocationHandler

        assertThat(proxyHello.sayHello("SR")).isEqualTo("HELLO SR");
        assertThat(proxyHello.sayHi("SR")).isEqualTo("HI SR");
        assertThat(proxyHello.sayThankYou("SR")).isEqualTo("THANK YOU SR");
    }
}