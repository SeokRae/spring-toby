package com.example.aop.adservice;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.TransientDataAccessResourceException;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@SpringBootTest(classes = UserServiceAopConfig.class)
class UserServiceImplTest {

    @Autowired
    private UserServiceImpl.TestUserService testUserService;

    @DisplayName("읽기전용 메서드에 쓰기 작업을 추가하여 읽기전용 속성 위반에 대한 예외 확인 테스트")
    @Test
    void testCase1() {
        assertThatExceptionOfType(TransientDataAccessResourceException.class)
                .isThrownBy(() -> testUserService.getAll());
    }
}