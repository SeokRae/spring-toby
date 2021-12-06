package com.example.service.part1;

import com.example.service.config.DataSourceConfig;
import com.example.service.domain.Level;
import com.example.service.domain.User;
import com.example.service.exception.TestUserServiceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.List;

import static com.example.service.part1.UserService.MIN_LOGCOUNT_FOR_SILVER;
import static com.example.service.part1.UserService.MIN_RECCOMEND_FOR_GOLD;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        DataSourceConfig.class,
        UserDaoJdbc.class,
        UserService.class
})
class UserServiceTest {

    private PlatformTransactionManager transactionManager;

    @Autowired
    private UserService userService;

    @Autowired
    private UserDao userDao;

    @Autowired
    private DataSource dataSource;

    private List<User> users;

    @BeforeEach
    void setUp() {
        transactionManager = new DataSourceTransactionManager(dataSource);
        userService.setUserDao(userDao);
        userService.setTransactionManager(transactionManager);
        userDao.deleteAll();
        users = Arrays.asList(
                new User("user1", "username1", "u1", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER - 1, 0)
                , new User("user2", "username2", "u2", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER, 0)
                , new User("user3", "username3", "u3", Level.SILVER, 60, MIN_RECCOMEND_FOR_GOLD - 1)
                , new User("user4", "username4", "u4", Level.SILVER, 60, MIN_RECCOMEND_FOR_GOLD)
                , new User("user5", "username5", "u5", Level.GOLD, 100, Integer.MAX_VALUE)
        );
    }

    @DisplayName("빈 주입 테스트")
    @Test
    void bean_di_expected_success() {
        assertThat(this.userService).isNotNull();
    }

    @DisplayName("사용자 레벨 업그레이드 테스트")
    @Test
    void user_level_update_expected_success() {
        for (User user : users) {
            userDao.add(user);
        }

        userService.upgradeLevels();

        checkLevel(users.get(0), Level.BASIC);
        checkLevel(users.get(1), Level.SILVER);
        checkLevel(users.get(2), Level.SILVER);
        checkLevel(users.get(3), Level.GOLD);
        checkLevel(users.get(4), Level.GOLD);
    }

    private void checkLevel(User user, Level expectedLevel) {
        User upgradeUser = userDao.get(user.getId());
        assertThat(upgradeUser.getLevel()).isEqualTo(expectedLevel);
    }

    @DisplayName("Level 필드 저장 확인 테스트")
    @Test
    void check_level_expected_success_field_settings() {

        User userWithLevel = users.get(4);
        User userWithOutLevel = users.get(0);
        userWithOutLevel.setLevel(null);

        userService.add(userWithLevel);
        userService.add(userWithOutLevel);

        User userWithLevelRead = userDao.get(userWithLevel.getId());
        User userWithOutLevelRead = userDao.get(userWithOutLevel.getId());

        assertThat(userWithLevelRead.getLevel()).isEqualTo(userWithLevel.getLevel());
        assertThat(userWithOutLevelRead.getLevel()).isEqualTo(userWithOutLevel.getLevel());
    }

    @DisplayName("Level 필드 개선 테스트")
    @Test
    void upgrade_levels() {
        for (User user : users) {
            userDao.add(user);
        }

        userService.upgradeLevels();

        checkLevelUpgraded(users.get(0), false);
        checkLevelUpgraded(users.get(1), true);
        checkLevelUpgraded(users.get(2), false);
        checkLevelUpgraded(users.get(3), true);
        checkLevelUpgraded(users.get(4), false);
    }

    private void checkLevelUpgraded(User user, boolean upgraded) {
        User userUpdate = userDao.get(user.getId());

        if (upgraded) {
            assertThat(userUpdate.getLevel()).isEqualTo(user.getLevel().nextLevel());
        } else {
            assertThat(userUpdate.getLevel()).isEqualTo(user.getLevel());
        }
    }

    @DisplayName("트랜잭션을 검증하기 위한 테스트")
    @Test
    void upgradeAllOrNothing() {
        UserService testUserService = new TestUserService(users.get(3).getId());
        testUserService.setUserDao(this.userDao);
        testUserService.setTransactionManager(transactionManager);

        userDao.deleteAll();
        for (User user : users) {
            userDao.add(user);
        }

        assertThatExceptionOfType(TestUserServiceException.class)
                .isThrownBy(testUserService::upgradeLevels);

        checkLevelUpgraded(users.get(1), false);
    }
}