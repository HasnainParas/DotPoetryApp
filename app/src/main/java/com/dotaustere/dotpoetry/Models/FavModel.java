package com.dotaustere.dotpoetry.Models;

public class FavModel {

    String user, poetry, poetryId;

    public FavModel() {

    }

    public FavModel(String user, String poetry, String poetryId) {
        this.user = user;
        this.poetry = poetry;
        this.poetryId = poetryId;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPoetry() {
        return poetry;
    }

    public void setPoetry(String poetry) {
        this.poetry = poetry;
    }

    public String getPoetryId() {
        return poetryId;
    }

    public void setPoetryId(String poetryId) {
        this.poetryId = poetryId;
    }
}