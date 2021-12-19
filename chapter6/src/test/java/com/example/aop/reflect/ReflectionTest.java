package com.example.aop.reflect;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.assertj.core.api.Assertions.assertThat;

class ReflectionTest {

    @DisplayName("String 클래스의 문자열 길이 확인 테스트")
    @Test
    void testCase1() {
        String name = "Spring";

        assertThat(name).hasSize(6);
    }

    @DisplayName("리플렉션을 통한 문자열 길이 확인 테스트")
    @Test
    void testCase2() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String name = "String";

        Method lengthMethod = String.class.getMethod("length");
        assertThat(lengthMethod.invoke(name)).isEqualTo(6);
    }

    @DisplayName("리플렉션을 통한 charAt() 메서드 값 확인 테스트")
    @Test
    void testCase3() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String name = "String";

        Method charAtMethod = String.class.getMethod("charAt", int.class);

        assertThat(charAtMethod.invoke(name, 0)).isEqualTo('S');
    }
}
