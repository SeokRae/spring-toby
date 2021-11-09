package com.example.chapter1.part1.dao.concrete;

import com.example.chapter1.part1.dao.ConnectionMaker;
import com.example.chapter1.part1.dao.UserDao;

public class NUserDao extends UserDao {
    public NUserDao(ConnectionMaker connectionMaker) {
        super(connectionMaker);
    }
}
