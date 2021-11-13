package com.example.chapter2.dao;

import com.example.chapter2.config.DataSourceConfig;
import com.example.chapter2.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;

import javax.sql.DataSource;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class UserDao2Test {

    @DisplayName("사용자 등록 및 조회 테스트")
    @Test
    void addAndGet() throws SQLException {
        ApplicationContext context = new AnnotationConfigApplicationContext(DataSourceConfig.class);
        DataSource dataSource = context.getBean("dataSource", DataSource.class);

        UserDao userDao = new UserDao(dataSource);

        userDao.truncateTable();
        assertThat(userDao.getCount()).isZero();

        User actual = new User("user_id", "user_name", "user_password");
        userDao.add(actual);
        assertThat(userDao.getCount()).isOne();

        User expected = userDao.get("user_id");
        assertThat(actual.getName()).isEqualTo(expected.getName());
    }

    @DisplayName("데이터 액세스 예외 테스트")
    @Test
    void testCase3() throws SQLException {
        ApplicationContext context = new AnnotationConfigApplicationContext(DataSourceConfig.class);
        DataSource dataSource = context.getBean("dataSource", DataSource.class);

        UserDao userDao = new UserDao(dataSource);

        userDao.truncateTable();
        assertThat(userDao.getCount()).isZero();

        User actual = new User("user_id", "user_name", "user_password");
        userDao.add(actual);

        assertThatExceptionOfType(EmptyResultDataAccessException.class)
                .isThrownBy(() -> userDao.get("unknown_id"));
    }
}