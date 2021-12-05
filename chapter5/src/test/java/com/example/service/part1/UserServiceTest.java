package com.example.service.part1;

import com.example.service.config.DataSourceConfig;
import com.example.service.domain.Level;
import com.example.service.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        DataSourceConfig.class,
        UserDaoJdbc.class,
        UserService.class
})
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserDao userDao;

    private List<User> users;

    @BeforeEach
    void setUp() {
        userDao.deleteAll();
        users = Arrays.asList(
                new User("user1", "username1", "u1", Level.BASIC, 49, 0)
                , new User("user2", "username2", "u2", Level.BASIC, 50, 0)
                , new User("user3", "username3", "u3", Level.SILVER, 60, 29)
                , new User("user4", "username4", "u4", Level.SILVER, 60, 30)
                , new User("user5", "username5", "u5", Level.GOLD, 100, 100)
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
}