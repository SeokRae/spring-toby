package com.example.service.part1;

import com.example.service.domain.Level;
import com.example.service.domain.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserService {
    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public void upgradeLevels() {
        List<User> users = userDao.getAll();

        for (User user : users) {
            boolean changed;
            if(user.getLevel() == Level.BASIC && user.getLogin() >= 50) {
                user.setLevel(Level.SILVER);
                changed = true;
            } else if(user.getLevel() == Level.SILVER && user.getRecommend() >= 30) {
                user.setLevel(Level.GOLD);
                changed = true;
            } else if(user.getLevel() == Level.GOLD) {
                changed = false;
            } else {
                changed = false;
            }
            if(changed) {
                userDao.update(user);
            }
        }

    }

    public void add(User user) {
        if(user.getLevel() == null) {
            user.setLevel(Level.BASIC);
        }
        userDao.add(user);
    }
}
