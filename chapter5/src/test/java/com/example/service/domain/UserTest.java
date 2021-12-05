package com.example.service.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class UserTest {

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
    }

    @DisplayName("사용자 레벨 업그레이드 테스트")
    @Test
    void upgrade_level() {
        Level[] levels = Level.values();

        for(Level level : levels) {
            if(level.nextLevel() == null) continue;
            user.setLevel(level);

            user.upgradeLevel();
            assertThat(user.getLevel()).isEqualTo(level.nextLevel());
        }
    }

    @DisplayName("예외 테스트")
    @Test
    void cannotUpgradeLevel() {
        Level[] levels = Level.values();
        for(Level level : levels) {
            if(level.nextLevel() != null) continue;
            user.setLevel(level);
            assertThatExceptionOfType(IllegalStateException.class)
                    .isThrownBy(() -> user.upgradeLevel());
        }
    }
}