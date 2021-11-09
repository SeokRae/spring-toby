package com.example.chapter1.part1.factory;

import com.example.chapter1.part1.dao.DConnectionMaker;
import com.example.chapter1.part1.dao.NConnectionMaker;
import com.example.chapter1.part1.dao.UserDao;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration // 애플리케이션 컨텍스트 또는 빈 팩토리가 사용할 설정정보라는 표시
public class DaoFactory {

    @Bean // 오브젝트 생성을 담당하는 IoC용 메서드라는 표시
    public UserDao dUserDao() {
        return new UserDao(dConnectionMaker());
    }

    @Bean
    public UserDao nUserDao() {
        return new UserDao(nConnectionMaker());
    }

    @Bean
    public DConnectionMaker dConnectionMaker() {
        return new DConnectionMaker();
    }

    @Bean
    public NConnectionMaker nConnectionMaker() {
        return new NConnectionMaker();
    }
}
