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

import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        DataSourceConfig.class,
        UserDaoJdbc.class
})
class UserDaoJdbcTest {

    @Autowired
    private UserDao userDao;
    private User user1;
    private User user2;
    private User user3;

    @BeforeEach
    void setUp() {
        userDao.deleteAll();
        user1 = new User("user1", "username1", "1234", Level.BASIC, 1, 0);
        user2 = new User("user2", "username2", "1234", Level.SILVER, 55, 10);
        user3 = new User("user3", "username3", "1234", Level.GOLD, 100, 40);
    }

    @DisplayName("사용자 등록 테스트")
    @Test
    void user_register_test_expected_new_user() {

        assertThat(userDao.getCount()).isZero();

        userDao.add(user1);
        userDao.add(user2);
        userDao.add(user3);

        assertThat(userDao.getCount()).isEqualTo(3);

        User expected1 = userDao.get(user1.getId());
        checkSameUser(user1, expected1);

        User expected2 = userDao.get(user2.getId());
        checkSameUser(user2, expected2);
    }

    private void checkSameUser(User actual, User expected) {
        assertThat(actual.getId()).isEqualTo(expected.getId());
        assertThat(actual.getName()).isEqualTo(expected.getName());
        assertThat(actual.getPassword()).isEqualTo(expected.getPassword());
        assertThat(actual.getLevel()).isEqualTo(expected.getLevel());
        assertThat(actual.getLogin()).isEqualTo(expected.getLogin());
        assertThat(actual.getRecommend()).isEqualTo(expected.getRecommend());
    }

    @DisplayName("사용자 정보 수정 테스트")
    @Test
    void user_update_expected_updated_user_info() {

        assertThat(userDao.getCount()).isZero();

        userDao.add(user1);

        user1.setName("seok");
        user1.setPassword("1234");
        user1.setLogin(1000);
        user1.setRecommend(999);
        user1.setLevel(Level.GOLD);
        userDao.update(user1);

        User user = userDao.get(user1.getId());
        checkSameUser(user1, user);
    }
}