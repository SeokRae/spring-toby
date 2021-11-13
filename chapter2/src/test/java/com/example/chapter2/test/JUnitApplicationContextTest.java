package com.example.chapter2.test;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@ContextConfiguration
class JUnitApplicationContextTest {
    // 테스트 컨텍스트가 매번 주입해주는 애플리케이션 컨텍스트는 항상 같은 오브젝트인지 테스트로 확인
    @Autowired
    private ApplicationContext context;
    static Set<JUnitApplicationContextTest> testObjects = new HashSet<>();
    static ApplicationContext contextObject = null;

    @DisplayName("애플리케이션 컨텍스트가 context 변수에 주입되었는지 확인하는 테스트")
    @Test
    void testCase1() {
        assertThat(testObjects).doesNotHaveSameClassAs(this);
        testObjects.add(this);

        assertThat(contextObject == null || contextObject == this.context).isTrue();
        contextObject = this.context;
    }

    @DisplayName("애플리케이션 컨텍스트가 context 변수에 주입되었는지 확인하는 테스트")
    @Test
    void testCase2() {
        assertThat(testObjects).doesNotHaveSameClassAs(this);
        testObjects.add(this);

        assertThat(contextObject == null || contextObject == this.context).isTrue();
        contextObject = this.context;
    }

    @DisplayName("애플리케이션 컨텍스트가 context 변수에 주입되었는지 확인하는 테스트")
    @Test
    void testCase3() {
        assertThat(testObjects).doesNotHaveSameClassAs(this);
        testObjects.add(this);

        // assertThat(contextObject, either(is(nullValue())).or(is(this.context)));
        // contextObject = this.context;
    }
}
