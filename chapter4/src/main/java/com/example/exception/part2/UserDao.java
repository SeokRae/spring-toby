package com.example.exception.part2;

import com.example.exception.domain.User;

import java.sql.SQLException;
import java.util.List;

public interface UserDao {
    void add(User user);

    User get(String id) throws SQLException;

    List<User> getAll();

    void truncateTable() throws SQLException;

    int getCount() throws SQLException;
}
