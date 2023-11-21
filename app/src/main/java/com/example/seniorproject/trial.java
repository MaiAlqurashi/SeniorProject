package com.example.seniorproject;

import java.io.Serializable;

public class trial implements Serializable {
    private String username;

    public trial(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
