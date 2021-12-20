package com.example.aop.factory;

import com.example.aop.service.UserService;
import com.example.aop.service.UserServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

@Configuration
public class TxProxyFactoryConfig {

    @Bean
    public UserService userService() throws Exception {
        TxProxyFactoryBean txProxyFactoryBean = new TxProxyFactoryBean();
        txProxyFactoryBean.setTarget(new UserServiceImpl());
        txProxyFactoryBean.setTransactionManager(new DataSourceTransactionManager());
        txProxyFactoryBean.setServiceInterface(UserService.class);
        txProxyFactoryBean.setPattern("upgradeLevels");
        return (UserService) txProxyFactoryBean.getObject();
    }
}
