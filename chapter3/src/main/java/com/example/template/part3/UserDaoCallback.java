package com.example.template.part3;

import com.example.template.part1.User;
import com.example.template.part1.strategy.StatementStrategy;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class UserDaoCallback {
    private RowMapper<User> userMapper =
            new RowMapper<User>() {
                @Override
                public User mapRow(ResultSet rs, int rowNum) throws SQLException {
                    User user = new User();
                    user.setId(rs.getString("id"));
                    user.setName(rs.getString("name"));
                    user.setPassword(rs.getString("password"));
                    return user;
                }
            };

    private JdbcTemplate jdbcTemplate;

    public UserDaoCallback(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void add(User user) {
        this.jdbcTemplate.update(
                "insert into users(id, name, password) values(?, ?, ?)"
                , user.getId()
                , user.getName()
                , user.getPassword()
        );
    }

    public User get(String id) {
        return this.jdbcTemplate.queryForObject(
                "select * from users where id = ?"
                , new Object[]{id}
                , this.userMapper
        );
    }

    public void deleteAll() {
        this.jdbcTemplate.update("delete from users");
    }

    public int getCount() {
        return this.jdbcTemplate.query(
                "select count(*) from users"
                ,
                rs -> {
                    rs.next();
                    return rs.getInt(1);
                }
        );
    }

    public List<User> getAll() {
        return this.jdbcTemplate.query(
                "select * from users order by id"
                , this.userMapper
        );
    }
}
