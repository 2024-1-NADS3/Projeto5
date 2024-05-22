package com.nutrienviroment.nutrigenda.models.user;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class User {
    private String id;
    private String name;
    private String email;
    private String password;
    private String nutritionistId;

    public User(String name, String email, String password, String nutritionistId) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.nutritionistId = nutritionistId;
    }

    public User(String name, String email, String password) {
        this(name, email, password, null);
    }

    public User(String email, String password) {
        this(null, email, password, null);
    }
}
