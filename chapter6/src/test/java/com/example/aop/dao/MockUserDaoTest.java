package com.example.aop.dao;

import com.example.aop.domain.Level;
import com.example.aop.domain.User;
import com.example.aop.mail.MockMailSender;
import com.example.aop.service.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static com.example.aop.service.UserServiceImpl.MIN_LOGCOUNT_FOR_SILVER;
import static com.example.aop.service.UserServiceImpl.MIN_RECCOMEND_FOR_GOLD;
import static org.assertj.core.api.Assertions.assertThat;

class MockUserDaoTest {

    private List<User> users;

    @BeforeEach
    void setUp() {
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
    void upgradeLevels() {
        UserServiceImpl userService = new UserServiceImpl();

        MockUserDao mockUserDao = new MockUserDao(this.users);
        userService.setUserDao(mockUserDao);

        // 메일 발송 여부 확인을 위해 목 오브젝트 DI
        MockMailSender mockMailSender = new MockMailSender();
        userService.setMailSender(mockMailSender);

        userService.upgradeLevels();

        // 레벨이 갱신된 사용자 정보에 대한 값 검증
        List<User> updated = mockUserDao.getUpdated();
        assertThat(updated.size()).isEqualTo(2);
        checkUserAndLevel(updated.get(0), "user2", Level.SILVER);
        checkUserAndLevel(updated.get(1), "user4", Level.GOLD);

        // 목 오브젝트를 이용한 결과 확인
        List<String> requests = mockMailSender.getRequests();

        assertThat(requests.size()).isEqualTo(2);
        assertThat(requests.get(0)).isEqualTo(users.get(1).getEmail());
        assertThat(requests.get(1)).isEqualTo(users.get(3).getEmail());
    }

    private void checkUserAndLevel(User updated, String expectedId, Level expectedLevel) {
        assertThat(updated.getId()).isEqualTo(expectedId);
        assertThat(updated.getLevel()).isEqualTo(expectedLevel);
    }

}