package com.example.aop.factory;

import com.example.aop.service.UserService;
import com.example.aop.service.UserServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.sql.Driver;

@Configuration
public class TxProxyFactoryConfig {

    @Bean
    public UserService userService() throws Exception {
        TxProxyFactoryBean txProxyFactoryBean = new TxProxyFactoryBean();
        txProxyFactoryBean.setTarget(userServiceImpl());
        txProxyFactoryBean.setTransactionManager(transactionManager());
        txProxyFactoryBean.setServiceInterface(UserService.class);
        txProxyFactoryBean.setPattern("upgradeLevels");
        return (UserService) txProxyFactoryBean.getObject();
    }

    @Bean
    public PlatformTransactionManager transactionManager() throws ClassNotFoundException {
        return new DataSourceTransactionManager(dataSource());
    }

    @Bean
    public DataSource dataSource() throws ClassNotFoundException {
        SimpleDriverDataSource dataSource = new SimpleDriverDataSource();

        dataSource.setDriverClass((Class<? extends Driver>) Class.forName("com.mysql.cj.jdbc.Driver"));
        dataSource.setUrl("jdbc:mysql://localhost:3309/user-db");
        dataSource.setUsername("root");
        dataSource.setPassword("1234");

        return dataSource;
    }

    @Bean
    public UserService userServiceImpl() {
        return new UserServiceImpl();
    }
}
