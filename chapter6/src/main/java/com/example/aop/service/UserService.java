package com.example.aop.service;

import com.example.aop.domain.User;

public interface UserService {
    void add(User user);

    void upgradeLevels();
}
