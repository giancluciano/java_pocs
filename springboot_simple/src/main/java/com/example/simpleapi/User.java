package com.example.simpleapi;

import org.springframework.data.annotation.Id;

public class User {
    private final @Id Long id;
    private String username, password;

    public User(String username, String password){
        this.id = null;
        this.username = username;
        this.password = password;

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    

}
