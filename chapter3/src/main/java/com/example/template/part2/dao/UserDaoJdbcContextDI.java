package com.example.template.part2.dao;

import com.example.template.part1.User;
import com.example.template.part1.strategy.AddStatement;
import com.example.template.part1.strategy.StatementStrategy;
import com.example.template.part1.strategy.TruncateStatement;
import com.example.template.part2.context.JdbcContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.EmptyResultDataAccessException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
