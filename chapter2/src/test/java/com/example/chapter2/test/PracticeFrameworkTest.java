package com.example.chapter2.test;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * JUnit은 테스트 실행 시 각 메서드 마다 새로운 테스트 오브젝트를 만든다.
 */
class PracticeFrameworkTest {
    static Set<PracticeFrameworkTest> testObjects =
            new HashSet<>();

    @DisplayName("JUnit이 매번 새로운 테스트 오브젝트를 만든다는 사실을 검증하는 테스트")
    @RepeatedTest(value = 3, name = "{currentRepetition} / {totalRepetitions}")
    void testCase1() {
        assertThat(testObjects).doesNotHaveSameClassAs(this);
        testObjects.add(this);
    }
}
