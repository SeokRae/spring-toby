package com.example.aop.adservice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = UserServiceAopConfig.class)
class UserServiceImplTest {

    @Autowired
    private UserServiceImpl.TestUserService testUserService;

    @Test
    void testCase1() {
        testUserService.getAll();
    }
}