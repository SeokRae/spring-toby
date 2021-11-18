package com.example.template.part1.diff;

import com.example.template.part1.User;
import com.example.template.part1.strategy.StatementStrategy;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserDaoAnonymousInnerClass {

    private final DataSource dataSource;

    public UserDaoAnonymousInnerClass(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void add(final User user) throws SQLException {
        jdbcContextWithStatementStrategy(new StatementStrategy() {
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
        });
    }

    public void truncateTable() throws SQLException {
        jdbcContextWithStatementStrategy(new StatementStrategy() {
            @Override
            public PreparedStatement makePreparedStatement(Connection c) throws SQLException {
                return c.prepareStatement("TRUNCATE TABLE USERS");
            }
        });
    }

    public void jdbcContextWithStatementStrategy(StatementStrategy strategy) throws SQLException {
        try (
                Connection c = dataSource.getConnection();
                PreparedStatement s = strategy.makePreparedStatement(c)
        ) {
            s.executeUpdate();
        }
    }
}
