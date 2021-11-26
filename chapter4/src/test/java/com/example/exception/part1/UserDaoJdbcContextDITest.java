package com.example.exception.part1;

import com.example.exception.config.DataSourceConfig;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.SQLException;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        DataSourceConfig.class,
        JdbcContext.class,
        UserDaoJdbcContextDI.class
})
class UserDaoJdbcContextDITest {

    @Autowired
    private UserDaoJdbcContextDI userDao;

    private User user1;
    private User user2;
    private User user3;

    @BeforeEach
    void setUp() throws SQLException {

        userDao.truncateTable();

        user1 = new User("user_0", "user_name_0", "1234");
        user2 = new User("user_1", "user_name_1", "1234");
        user3 = new User("user_2", "user_name_2", "1234");
    }

    @DisplayName("사용자 등록 테스트")
    @Test
    void testCase1() {

    }
}