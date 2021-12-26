package com.example.aop.service;

import com.example.aop.dao.UserDao;
import com.example.aop.dao.UserDaoJdbc;
import com.example.aop.domain.Level;
import com.example.aop.domain.User;
import com.example.aop.exception.TestUserServiceException;
import com.example.aop.mail.MockMailSender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.sql.DataSource;
import java.sql.Driver;
import java.util.Arrays;
import java.util.List;

import static com.example.aop.service.UserServiceImpl.MIN_LOG_COUNT_FOR_SILVER;
import static com.example.aop.service.UserServiceImpl.MIN_RECOMMEND_FOR_GOLD;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        TestUserServiceImplTest.TestUserServiceImplConfiguration.class
})
class TestUserServiceImplTest {

    @Autowired
    private UserService testUserService;

    @Autowired
    private UserDao userDao;

    private List<User> users;

    @BeforeEach
    void setUp() {
        users = Arrays.asList(
                new User("user1", "user1@gmail.com", "username1", "u1", Level.BASIC, MIN_LOG_COUNT_FOR_SILVER - 1, 0)
                , new User("user2", "user2@gmail.com", "username2", "u2", Level.BASIC, MIN_LOG_COUNT_FOR_SILVER, 0)
                , new User("user3", "user3@gmail.com", "username3", "u3", Level.SILVER, 60, MIN_RECOMMEND_FOR_GOLD - 1)
                , new User("user4", "user4@gmail.com", "username4", "u4", Level.SILVER, 60, MIN_RECOMMEND_FOR_GOLD)
                , new User("user5", "user5@gmail.com", "username5", "u5", Level.GOLD, 100, Integer.MAX_VALUE)
        );
    }

    @DisplayName("userService 빈을 자동으로 트랜잭션 부가기능을 제공해주는 프록시로 대체 했는지 확인하는 테스트")
    @Test
    void testCase1() {
        userDao.deleteAll();
        for (User user : users) userDao.add(user);

        assertThatExceptionOfType(TestUserServiceException.class)
                .isThrownBy(() -> this.testUserService.upgradeLevels());

        checkLevelUpgraded(users.get(1), false);
    }

    private void checkLevelUpgraded(User user, boolean upgraded) {
        User userUpdate = userDao.get(user.getId());

        if (upgraded) {
            assertThat(userUpdate.getLevel()).isEqualTo(user.getLevel().nextLevel());
        } else {
            assertThat(userUpdate.getLevel()).isEqualTo(user.getLevel());
        }
    }

    static class TestUserServiceImpl extends UserServiceImpl {
        private final String id = "user2";

        @Override
        protected void upgradeLevel(User user) {
            if (user.getId().equals(this.id)) throw new TestUserServiceException();
            super.upgradeLevel(user);
        }
    }

    @TestConfiguration
    static class TestUserServiceImplConfiguration {

        @Bean
        public UserService testUserService() throws ClassNotFoundException {
            TestUserServiceImpl testUserService = new TestUserServiceImpl();
            testUserService.setUserDao(userDao());
            testUserService.setMailSender(new MockMailSender());
            return testUserService;
        }

        @Bean
        public UserService userService() throws ClassNotFoundException {
            UserServiceImpl userService = new UserServiceImpl();
            userService.setUserDao(userDao());
            userService.setMailSender(new MockMailSender());
            return userService;
        }

        @Bean
        public UserDao userDao() throws ClassNotFoundException {
            return new UserDaoJdbc(dataSource());
        }

        @Bean
        public DataSource dataSource() throws ClassNotFoundException {
            SimpleDriverDataSource dataSource = new SimpleDriverDataSource();

            dataSource.setDriverClass((Class<? extends Driver>) Class.forName("com.mysql.cj.jdbc.Driver"));
            dataSource.setUrl("jdbc:mysql://localhost:3309/user-db");
            dataSource.setUsername("root");
            dataSource.setPassword("1234");

            return dataSource;
        }
    }
}