package com.example.chapter1.part1.dao;

import com.example.chapter1.part1.domain.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class NUserDao extends UserDao{
    @Override
    public Connection getConnection() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection("jdbc:mysql://localhost:3309/user-db", "root", "1234");
    }

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        UserDao dao = new NUserDao();

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
