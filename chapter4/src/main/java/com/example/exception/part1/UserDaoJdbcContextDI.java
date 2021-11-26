package com.example.exception.part1;

import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Component
public class UserDaoJdbcContextDI {

    private final JdbcContext context;

    public UserDaoJdbcContextDI(JdbcContext context) {
        this.context = context;
    }

    public void add(User user) throws SQLException {
        this.context.workWithStatementStrategy(
                new StatementStrategy() {
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
        );
    }

    public void truncateTable() throws SQLException {
        this.context.executeSql("TRUNCATE TABLE USERS");
    }
}
