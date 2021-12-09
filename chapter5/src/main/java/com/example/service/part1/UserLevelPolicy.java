package com.example.service.part1;

import com.example.service.domain.User;

public interface UserLevelPolicy {
    boolean canUpgradeLevel(User user);

    void upgradeLevel(User user);
}
