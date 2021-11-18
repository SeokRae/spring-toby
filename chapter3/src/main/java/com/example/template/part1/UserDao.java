package com.example.template.part1;

import com.example.template.part1.strategy.AddStatement;
import com.example.template.part1.strategy.StatementStrategy;
import com.example.template.part1.strategy.TruncateStatement;
import org.springframework.dao.EmptyResultDataAccessException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao {

    private final DataSource dataSource;

    public UserDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void add(User user) throws SQLException {
        StatementStrategy st = new AddStatement(user);
        jdbcContextWithStatementStrategy(st);
    }

    // 인터페이스 DI
    public void truncateTable() throws SQLException {
        StatementStrategy st = new TruncateStatement();
        jdbcContextWithStatementStrategy(st);
    }

    public void jdbcContextWithStatementStrategy(StatementStrategy strategy) throws SQLException {
        try (
                Connection c = dataSource.getConnection();
                PreparedStatement s = strategy.makePreparedStatement(c);
        ) {
            s.executeUpdate();
        }
    }

    public User get(String id) throws SQLException {
        try (
                Connection c = dataSource.getConnection();
                PreparedStatement ps = c.prepareStatement("SELECT * FROM USERS WHERE id = ?")
        ) {
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();

            User user = null;

            if (rs.next()) {
                user = new User();
                user.setId(rs.getString("id"));
                user.setName(rs.getString("name"));
                user.setPassword(rs.getString("password"));
            }

            if (user == null) {
                throw new EmptyResultDataAccessException(1);
            }
            return user;
        }
    }

    public int getCount() throws SQLException {
        try (
                Connection c = dataSource.getConnection();
                PreparedStatement ps = c.prepareStatement("SELECT COUNT(*) FROM USERS")
        ) {
            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getInt(1);
        }
    }
}
