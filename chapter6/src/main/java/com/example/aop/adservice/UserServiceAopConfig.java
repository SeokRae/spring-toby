package com.example.aop.adservice;

import org.aspectj.lang.annotation.Pointcut;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.interceptor.TransactionInterceptor;

import javax.sql.DataSource;
import java.sql.Driver;
import java.util.Properties;

@Configuration
@EnableAspectJAutoProxy
public class UserServiceAopConfig {

    @Bean
    public Advisor advisor() throws ClassNotFoundException {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression("bean(*Service)");
        return new DefaultPointcutAdvisor(pointcut, transactionAdvice());
    }

    @Bean
    public TransactionInterceptor transactionAdvice() throws ClassNotFoundException {
        Properties transactionAttributes = new Properties();
        transactionAttributes.setProperty("get*", "PROPAGATION_REQUIRED, readOnly");
        transactionAttributes.setProperty("*", "PROPAGATION_REQUIRED");

        TransactionInterceptor transactionInterceptor = new TransactionInterceptor();
        transactionInterceptor.setTransactionManager(transactionManager());

        transactionInterceptor.setTransactionAttributes(transactionAttributes);
        return transactionInterceptor;
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
}
