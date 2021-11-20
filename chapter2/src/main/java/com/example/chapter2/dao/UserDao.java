package com.example.chapter2.dao;

import com.example.chapter2.domain.User;
import org.springframework.dao.EmptyResultDataAccessException;

import javax.sql.DataSource;
import java.sql.*;

public class UserDao {

    private final DataSource dataSource;

    public UserDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void add(User user) throws SQLException {
        try (
                Connection c = dataSource.getConnection();
                PreparedStatement ps = c.prepareStatement("INSERT INTO USERS(id, name, password) values (?, ?, ?)")
        ) {
            ps.setString(1, user.getId());
            ps.setString(2, user.getName());
            ps.setString(3, user.getPassword());

            ps.executeUpdate();
        }
    }

    public User get(String id) throws SQLException {
        try (
                Connection c = dataSource.getConnection();
                PreparedStatement ps = makeStatement(c);
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

    private PreparedStatement makeStatement(Connection c) throws SQLException {
        return c.prepareStatement("SELECT * FROM USERS WHERE id = ?");
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

    public void truncateTable() throws SQLException {
        try (
                Connection c = dataSource.getConnection();
                Statement s = c.createStatement()
        ) {
            s.executeUpdate("TRUNCATE TABLE USERS");
        }
    }
}
