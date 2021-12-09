package com.example.service.part2;


import com.example.service.domain.Level;
import com.example.service.domain.User;
import com.example.service.dao.UserDao;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class UserDaoJdbc implements UserDao {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<User> userMapper =
            new RowMapper<User>() {
                @Override
                public User mapRow(ResultSet rs, int rowNum) throws SQLException {
                    User user = new User();
                    user.setId(rs.getString("id"));
                    user.setEmail(rs.getString("email"));
                    user.setName(rs.getString("name"));
                    user.setPassword(rs.getString("password"));
                    user.setLevel(Level.valueOf(rs.getInt("level")));
                    user.setLogin(rs.getInt("login"));
                    user.setRecommend(rs.getInt("recommend"));
                    return user;
                }
            };

    public UserDaoJdbc(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void add(User user) {
        this.jdbcTemplate.update(
                "insert into USERS(id, email, name, password, level, login, recommend) values(?, ?, ?, ?, ?, ?, ?)"
                , user.getId()
                , user.getEmail()
                , user.getName()
                , user.getPassword()
                , user.getLevel().intValue()
                , user.getLogin()
                , user.getRecommend()
        );
    }

    public User get(String id) {
        return this.jdbcTemplate.queryForObject(
                "select * from USERS where id = ?"
                , new Object[]{id}
                , this.userMapper
        );
    }

    public void deleteAll() {
        this.jdbcTemplate.update("delete from USERS");
    }

    public int getCount() {
        return this.jdbcTemplate.query(
                "select count(*) from USERS"
                , rs -> {
                    rs.next();
                    return rs.getInt(1);
                }
        );
    }

    public List<User> getAll() {
        return this.jdbcTemplate.query(
                "select * from USERS order by id"
                , this.userMapper
        );
    }

    public void update(User updateUser) {
        this.jdbcTemplate.update(
                "UPDATE USERS SET email = ?, name = ?, password = ?, level = ?, login = ?, recommend = ? where id = ?"
                , updateUser.getEmail(), updateUser.getName(), updateUser.getPassword()
                , updateUser.getLevel().intValue(), updateUser.getLogin(), updateUser.getRecommend()
                , updateUser.getId()
        );
    }
}
