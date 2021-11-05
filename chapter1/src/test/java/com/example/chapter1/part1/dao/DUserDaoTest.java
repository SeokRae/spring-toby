package com.example.chapter1.part1.dao;

import com.example.chapter1.part1.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

class DUserDaoTest {

    @DisplayName("D 회사 인사 정보 시스템 요청")
    @Test
    void when_d_dao() throws SQLException, ClassNotFoundException {
        UserDao dao = new DUserDao(new DConnectionMaker());

        User user = new User();
        user.setId("seok_id");
        user.setName("seok");
        user.setPassword("1234");

        dao.add(user);

        User user2 = dao.get(user.getId());

        assertThat(user).isEqualTo(user2);
    }
}