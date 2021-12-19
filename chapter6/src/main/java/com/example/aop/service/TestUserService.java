package com.example.aop.service;


import com.example.aop.domain.User;
import com.example.aop.exception.TestUserServiceException;

public class TestUserService extends UserServiceImpl {

    private final String id;

    public TestUserService(String id) {
        this.id = id;
    }

    @Override
    protected void upgradeLevel(User user) {
        if (user.getId().equals(this.id)) {
            throw new TestUserServiceException();
        }
        super.upgradeLevel(user);
    }
}
