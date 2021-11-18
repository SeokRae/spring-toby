package com.example.template.part1.strategy;

import com.example.template.part1.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AddStatement implements StatementStrategy {
    private final User user;

    public AddStatement(User user) {
        this.user = user;
    }

    @Override
    public PreparedStatement makePreparedStatement(Connection c) throws SQLException {
        try (
                PreparedStatement ps = c.prepareStatement("INSERT INTO USERS(id, name, password) values (?, ?, ?)")
        ) {
            ps.setString(1, user.getId());
            ps.setString(2, user.getName());
            ps.setString(3, user.getPassword());

            ps.executeUpdate();
            return ps;
        }
    }
}
