package com.example.chapter1.part1.dao;

import com.example.chapter1.part1.domain.User;
import com.example.chapter1.part1.factory.DaoFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

class UserDaoTest {

    @DisplayName("D 회사 인사 정보 시스템 요청")
    @Test
    void when_d_dao() throws SQLException, ClassNotFoundException {
        UserDao dao = new DaoFactory().dUserDao();

        User user = new User();
        user.setId("seok_id");
        user.setName("seok");
        user.setPassword("1234");

        dao.add(user);

        User user2 = dao.get(user.getId());

        dao.del();

        assertThat(user).isEqualTo(user2);
    }

    @DisplayName("N 회사 인사 정보 시스템 호출")
    @Test
    void when_n_dao() throws SQLException, ClassNotFoundException {
        UserDao dao = new DaoFactory().nUserDao();

        User user = new User();
        user.setId("seok_id");
        user.setName("seok");
        user.setPassword("1234");

        dao.add(user);

        User user2 = dao.get(user.getId());

        dao.del();

        assertThat(user).isEqualTo(user2);
    }

    @DisplayName("DaoFactory를 어플리케이션 컨텍스트의 설정정보로 사용하는 테스트")
    @Test
    void application_context_test() {
        ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
        UserDao dUserDao = context.getBean("dUserDao", UserDao.class);
        UserDao nUserDao = context.getBean("nUserDao", UserDao.class);

        assertThat(dUserDao).isNotNull();
        assertThat(nUserDao).isNotNull();
    }

    @DisplayName("객체의 동일성 실패 테스트")
    @Test
    void object_identity() {
        UserDao actual = new DaoFactory().dUserDao();
        UserDao expected = new DaoFactory().dUserDao();

        assertThat(actual).isNotEqualTo(expected);
    }

    @DisplayName("빈의 동일성 테스트")
    @Test
    void bean_identity() {
        ApplicationContext context = new AnnotationConfigApplicationContext(DaoFactory.class);
        UserDao actual = context.getBean("dUserDao", UserDao.class);
        UserDao expected = context.getBean("dUserDao", UserDao.class);

        assertThat(actual).isEqualTo(expected);
    }
}
