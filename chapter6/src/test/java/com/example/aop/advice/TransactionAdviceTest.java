package com.example.aop.advice;

import com.example.aop.config.DataSourceConfig;
import com.example.aop.dao.UserDao;
import com.example.aop.dao.UserDaoJdbc;
import com.example.aop.domain.Level;
import com.example.aop.domain.User;
import com.example.aop.exception.TestUserServiceException;
import com.example.aop.factory.TxProxyFactoryBean;
import com.example.aop.mail.MockMailSender;
import com.example.aop.service.TestUserService;
import com.example.aop.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;

import static com.example.aop.service.UserServiceImpl.MIN_LOG_COUNT_FOR_SILVER;
import static com.example.aop.service.UserServiceImpl.MIN_RECOMMEND_FOR_GOLD;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        TransactionAdviceConfig.class
})
class TransactionAdviceTest {

    @Autowired
    private ApplicationContext context;

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

    @DisplayName("트랜잭션을 검증하기 위한 테스트")
    @Test
    @DirtiesContext
    void upgradeAllOrNothing() {

        TestUserService testUserService = new TestUserService(users.get(3).getId());
        testUserService.setUserDao(userDao);
        testUserService.setMailSender(new MockMailSender());

        ProxyFactoryBean txProxyFactoryBean = context.getBean("$userService", ProxyFactoryBean.class);
        txProxyFactoryBean.setTarget(testUserService);
        UserService txUserService = (UserService) txProxyFactoryBean.getObject();

        userDao.deleteAll();

        for (User user : users) {
            userDao.add(user);
        }

        assertThatExceptionOfType(TestUserServiceException.class)
                .isThrownBy(txUserService::upgradeLevels);

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