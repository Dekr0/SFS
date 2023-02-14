package com.ece.sfs;

import org.springframework.security.core.userdetails.User;

import java.util.HashMap;

public class Group {
    private String name;
    private HashMap<String, User> users;

    public Group(String name) {
        this.name = name;
        this.users = new HashMap<>();
    }

    public Group(String name, HashMap<String, User> users) {
        this.name = name;
        this.users = users;
    }

    public void setName(String name) {
        if (name != null && !name.isEmpty()) {
            this.name = name;
        }

        throw new IllegalArgumentException("Name cannot be null or empty");
    }

    public String getName() {
        return name;
    }
}
