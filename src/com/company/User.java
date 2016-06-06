package com.company;


import java.util.ArrayList;

/**
 * Created by illladell on 6/6/16.
 */
public class User {
    String name;
    String password;
    ArrayList<Message> arrayList;

    public User(String name, String password) {
        this.name = name;
        this.password = password;
        arrayList = new ArrayList<>();
    }
}
