package com.example.service.part2;

import com.example.service.config.DataSourceConfig;
import com.example.service.dao.UserDao;
import com.example.service.domain.Level;
import com.example.service.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.List;

import static com.example.service.part2.UserService.MIN_LOGCOUNT_FOR_SILVER;
import static com.example.service.part2.UserService.MIN_RECCOMEND_FOR_GOLD;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        DataSourceConfig.class,
        UserDaoJdbc.class,
        UserService.class
})
@DirtiesContext // 컨텍스트의 DI 설정을 변경하는 테스트라는 것을 명시
class MockMailSenderTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserDao userDao;

    @Autowired
    private DataSource dataSource;

    private List<User> users;

    @BeforeEach
    void setUp() {
        userService.setTransactionManager(new DataSourceTransactionManager(dataSource));
        userService.setUserDao(userDao);

        userDao.deleteAll();
        users = Arrays.asList(
                new User("user1", "user1@gmail.com", "username1", "u1", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER - 1, 0)
                , new User("user2", "user2@gmail.com", "username2", "u2", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER, 0)
                , new User("user3", "user3@gmail.com", "username3", "u3", Level.SILVER, 60, MIN_RECCOMEND_FOR_GOLD - 1)
                , new User("user4", "user4@gmail.com", "username4", "u4", Level.SILVER, 60, MIN_RECCOMEND_FOR_GOLD)
                , new User("user5", "user5@gmail.com", "username5", "u5", Level.GOLD, 100, Integer.MAX_VALUE)
        );
    }

    @DisplayName("Level 필드 개선 테스트")
    @Test
    void upgrade_levels() {
        for (User user : users) {
            userDao.add(user);
        }

        MockMailSender mockMailSender = new MockMailSender();
        userService.setMailSender(mockMailSender);

        userService.upgradeLevels();

        checkLevelUpgraded(users.get(0), false);
        checkLevelUpgraded(users.get(1), true);
        checkLevelUpgraded(users.get(2), false);
        checkLevelUpgraded(users.get(3), true);
        checkLevelUpgraded(users.get(4), false);

        List<String> request = mockMailSender.getRequests();
        assertThat(request.size()).isEqualTo(2);
        assertThat(request.get(0)).isEqualTo(users.get(1).getEmail());
        assertThat(request.get(1)).isEqualTo(users.get(3).getEmail());
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