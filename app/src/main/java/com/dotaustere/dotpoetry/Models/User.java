package com.dotaustere.dotpoetry.Models;

public class User {
    private String uId,name,profile;

    public User() {
    }

    public User(String uId, String name, String profile) {
        this.uId = uId;
        this.name = name;
        this.profile = profile;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }
}
