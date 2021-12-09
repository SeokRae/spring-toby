package com.example.service.part2;

import com.example.service.config.DataSourceConfig;
import com.example.service.domain.Level;
import com.example.service.domain.User;
import com.example.service.exception.TestUserServiceException;
import com.example.service.part1.UserDao;
import com.example.service.part1.UserDaoJdbc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.mail.MailSender;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.List;

import static com.example.service.part2.UserService.MIN_LOGCOUNT_FOR_SILVER;
import static com.example.service.part2.UserService.MIN_RECCOMEND_FOR_GOLD;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        DataSourceConfig.class,
        UserDaoJdbc.class,
        DummyMailSender.class
})
class UserServiceTest {

    @Autowired
    private MailSender mailSender;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private UserDao userDao;

    private PlatformTransactionManager transactionManager;

    private List<User> users;

    @BeforeEach
    void setUp() {
        transactionManager = new DataSourceTransactionManager(dataSource);
        userDao.deleteAll();
        users = Arrays.asList(
                new User("user1", "user1@gmail.com", "username1", "u1", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER - 1, 0)
                , new User("user2", "user2@gmail.com", "username2", "u2", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER, 0)
                , new User("user3", "user3@gmail.com", "username3", "u3", Level.SILVER, 60, MIN_RECCOMEND_FOR_GOLD - 1)
                , new User("user4", "user4@gmail.com", "username4", "u4", Level.SILVER, 60, MIN_RECCOMEND_FOR_GOLD)
                , new User("user5", "user5@gmail.com", "username5", "u5", Level.GOLD, 100, Integer.MAX_VALUE)
        );
    }

    @DisplayName("트랜잭션을 검증하기 위한 테스트")
    @Test
    void upgradeAllOrNothing() {

        UserService testUserService = new TestUserService(users.get(3).getId());
        testUserService.setUserDao(this.userDao);
        testUserService.setTransactionManager(transactionManager);
        testUserService.setMailSender(mailSender);

        for (User user : users) {
            userDao.add(user);
        }
        assertThatExceptionOfType(TestUserServiceException.class)
                .isThrownBy(testUserService::upgradeLevels);

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
}