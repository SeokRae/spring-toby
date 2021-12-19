package com.example.aop.dao;

import com.example.aop.domain.User;

import java.util.ArrayList;
import java.util.List;

public class MockUserDao implements UserDao {
    // 레벨 업그레이드 후보 User 오브젝트 목록
    private final List<User> users;
    // 업그레이드 대상 오브젝트를 저장해둘 목록
    private final List<User> updated = new ArrayList<>();

    public MockUserDao(List<User> users) {
        this.users = users;
    }

    public List<User> getUpdated() {
        return updated;
    }

    // 스텁 기능 제공
    @Override
    public List<User> getAll() {
        return this.users;
    }

    // 목 오브젝트 기능을 제공
    @Override
    public void update(User updateUser) {
        updated.add(updateUser);
    }

    @Override
    public void add(User user) {
        throw new UnsupportedOperationException();
    }

    @Override
    public User get(String id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteAll() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getCount() {
        throw new UnsupportedOperationException();
    }
}
