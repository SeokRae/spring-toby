package com.example.chapter1.part1.factory;

import com.example.chapter1.part1.dao.ConnectionMaker;
import com.example.chapter1.part1.dao.CountingConnectionMaker;
import com.example.chapter1.part1.dao.DConnectionMaker;
import com.example.chapter1.part1.dao.UserDao;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CountingDaoFactory {

    @Bean // 오브젝트 생성을 담당하는 IoC용 메서드라는 표시
    public UserDao userDao() {
        return new UserDao(connectionMaker());
    }

    @Bean
    public ConnectionMaker connectionMaker() {
        return new CountingConnectionMaker(realConnectionMaker());
    }

    @Bean
    public ConnectionMaker realConnectionMaker() {
        return new DConnectionMaker();
    }

}
