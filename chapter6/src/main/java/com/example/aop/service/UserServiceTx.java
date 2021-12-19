package com.example.aop.service;

import com.example.aop.domain.User;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

public class UserServiceTx implements UserService {

    private PlatformTransactionManager transactionManager;
    private UserService userService;

    public void setTransactionManager(PlatformTransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void add(User user) {
        userService.add(user);
    }

    @Override
    public void upgradeLevels() {
        TransactionStatus transactionStatus = this.transactionManager.getTransaction(new DefaultTransactionDefinition());

        try {
            userService.upgradeLevels();

            this.transactionManager.commit(transactionStatus);
        } catch (RuntimeException e) {
            this.transactionManager.rollback(transactionStatus);
            throw e;
        }
    }
}
