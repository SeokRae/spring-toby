package com.example.chapter1.part2;

import com.example.chapter1.part1.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.sql.DataSource;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {DataSourceConfig.class})
class UserDaoTest {

    @Autowired
    private DataSource dataSource;

    @DisplayName("DataSource 인터페이스를 사용한 테스트")
    @Test
    void testCase1() throws SQLException {
        UserDao userDao = new UserDao(dataSource);

        User actual = new User();
        actual.setId("user_id");
        actual.setName("user_name");
        actual.setPassword("user_password");

        userDao.add(actual);

        User expected = userDao.get("user_id");

        userDao.del();

        assertThat(actual).isEqualTo(expected);
    }
}
