package com.example.chapter1.part1.dao;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * 기능 명세 인터페이스
 */
public interface ConnectionMaker {
    Connection makeConnection() throws ClassNotFoundException, SQLException;
}
