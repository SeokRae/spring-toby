package com.example.chapter2.dao;

import com.example.chapter2.config.DataSourceConfig;
import com.example.chapter2.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.sql.DataSource;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {DataSourceConfig.class})
class UserDaoTest {

    @Autowired
    private DataSource dataSource;
    private UserDao userDao;
    private User user1;
    private User user2;
    private User user3;

    @BeforeEach
    void setUp() throws SQLException {
        userDao = new UserDao(dataSource);
        userDao.truncateTable();

        user1 = new User("user_0", "user_name_0", "1234");
        user2 = new User("user_1", "user_name_1", "1234");
        user3 = new User("user_2", "user_name_2", "1234");
    }

    @DisplayName("테이블 데이터 등록 및 조회 테스트")
    @Test
    void testCase1() throws SQLException {

        assertThat(userDao.getCount()).isZero();

        userDao.add(user1);
        userDao.add(user2);
        assertThat(userDao.getCount()).isEqualTo(2);

        User expected1 = userDao.get(user1.getId());
        assertThat(user1.getName()).isEqualTo(expected1.getName());

        User expected2 = userDao.get(user2.getId());
        assertThat(user2.getName()).isEqualTo(expected2.getName());
    }

    @DisplayName("테이블 등록 카운트 테스트")
    @Test
    void testCase2() throws SQLException {
        assertThat(userDao.getCount()).isZero();

        userDao.add(user1);
        assertThat(userDao.getCount()).isOne();

        userDao.add(user2);
        assertThat(userDao.getCount()).isEqualTo(2);

        userDao.add(user3);
        assertThat(userDao.getCount()).isEqualTo(3);

        User actual = userDao.get("user_1");

        assertThat(actual.getName()).isEqualTo("user_name_1");
    }

    @DisplayName("데이터 액세스 예외 테스트")
    @Test
    void testCase3() throws SQLException {
        assertThat(userDao.getCount()).isZero();

        userDao.add(user1);

        assertThatExceptionOfType(EmptyResultDataAccessException.class)
                .isThrownBy(() -> userDao.get("111"));
    }
}