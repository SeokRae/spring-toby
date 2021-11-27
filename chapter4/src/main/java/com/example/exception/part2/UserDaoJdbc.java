package com.example.exception.part2;

import com.example.exception.domain.User;
import com.example.exception.part1.exception.DuplicateUserIdException;
import com.mysql.cj.exceptions.MysqlErrorNumbers;
import org.springframework.dao.EmptyResultDataAccessException;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;
import java.util.Objects;

public class UserDaoJdbc implements UserDao {

    private final DataSource dataSource;

    public UserDaoJdbc(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<User> getAll() {
        return null;
    }

    @Override
    public void add(User user) {
        try (
                Connection c = dataSource.getConnection();
                PreparedStatement ps = c.prepareStatement("INSERT INTO USERS(id, name, password) values (?, ?, ?)")
        ) {
            ps.setString(1, user.getId());
            ps.setString(2, user.getName());
            ps.setString(3, user.getPassword());

            ps.executeUpdate();
        } catch (SQLException e) {
            if (e.getErrorCode() == MysqlErrorNumbers.ER_DUP_ENTRY) {
                throw new DuplicateUserIdException(e);
            } else {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
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

            if (Objects.isNull(user)) {
                throw new EmptyResultDataAccessException(1);
            }
            return user;
        }
    }

    @Override
    public int getCount() throws SQLException {
        try (
                Connection c = dataSource.getConnection();
                PreparedStatement ps = c.prepareStatement("SELECT COUNT(*) FROM USERS");
        ) {
            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getInt(1);
        }
    }

    @Override
    public void truncateTable() throws SQLException {
        try (
                Connection c = dataSource.getConnection();
                Statement s = c.createStatement()
        ) {
            s.executeUpdate("TRUNCATE TABLE USERS");
        }
    }
}
