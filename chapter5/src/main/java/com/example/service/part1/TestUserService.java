package com.example.service.part1;

import com.example.service.domain.User;
import com.example.service.exception.TestUserServiceException;

public class TestUserService extends UserService {

    private final String id;

    public TestUserService(String id) {
        this.id = id;
    }

    @Override
    protected void upgradeLevel(User user) {
        if(user.getId().equals(this.id)) {
            throw new TestUserServiceException();
        }
        super.upgradeLevel(user);
    }
}
