package com.example.chapter1.part1.factory;

import com.example.chapter1.part1.dao.CountingConnectionMaker;
import com.example.chapter1.part1.dao.UserDao;
import com.example.chapter1.part1.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

class CountingDaoFactoryTest {

    @DisplayName("카운팅 커넥션 생성 테스트")
    @Test
    void testCase1() throws SQLException, ClassNotFoundException {
        AnnotationConfigApplicationContext context
                = new AnnotationConfigApplicationContext(CountingDaoFactory.class);

        UserDao userDao = context.getBean("userDao", UserDao.class);

        User user = new User();
        user.setId("user_id");
        user.setName("user_name");
        user.setPassword("user_password");

        userDao.add(user); // 1번 째 호출

        User findUser = userDao.get("user_id");// 2번 째 호출

        userDao.del(); // 3 번째 호출

        CountingConnectionMaker connectionMaker =
                context.getBean("connectionMaker", CountingConnectionMaker.class);

        int counter = connectionMaker.getCounter();

        assertThat(findUser.getId()).hasToString("user_id");
        assertThat(counter).isEqualTo(3);
    }
}