package com.eware.startup.service.mapper.pojo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class User {

    public static final String SYSTEM_USER = "System";
    private String username;
    private String avatar;

    public static User systemUser(){
        return new User(SYSTEM_USER, "https://robohash.org/system.png");
    }

    @JsonCreator
    public User(@JsonProperty("username") String username, @JsonProperty("avatar") String avatar) {
        this.username = username;
        this.avatar = avatar;
    }

    public String getUsername() {
        return username;
    }

    public String getAvatar() {
        return avatar;
    }

}