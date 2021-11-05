package com.example.chapter1.part1.dao;

import com.example.chapter1.part1.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

class NUserDaoTest {

    @DisplayName("N 회사 인사 정보 시스템 호출")
    @Test
    void when_n_dao() throws SQLException, ClassNotFoundException {
        UserDao dao = new NUserDao(new NConnectionMaker());

        User user = new User();
        user.setId("seok_id");
        user.setName("seok");
        user.setPassword("1234");

        dao.add(user);

        System.out.println("등록 성공 : " + user.getId());

        User user2 = dao.get(user.getId());
        System.out.println("사용자 명: " + user2.getName());
        System.out.println("사용자 비밀번호: " + user2.getPassword());

        System.out.println(user2.getId() + " : 조회 성공");
    }
}