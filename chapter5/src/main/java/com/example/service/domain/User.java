package com.example.service.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Table;

@Table(value = "USERS")
@Setter
@Getter
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
public class User {
    private String id;
    private String name;
    private String password;
    private Level level;
    private int login;
    private int recommend;

    public User(String id, String name, String password) {
        this(id, name, password, null, 0, 0);
    }

    public User(String id, String name, String password, Level level, int login, int recommend) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.level = level;
        this.login = login;
        this.recommend = recommend;
    }

    public void upgradeLevel() {
        Level nextLevel = this.level.nextLevel();
        if(nextLevel == null) {
            throw new IllegalStateException(this.level + "은 업그레이드가 불가능합니다.");
        } else {
            this.level = nextLevel;
        }
    }
}
