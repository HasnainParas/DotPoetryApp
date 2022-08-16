package com.dotaustere.dotpoetry.Models;

public class ShayariModel {

    String poetry,poetryId;

    public ShayariModel() {
    }

    public ShayariModel(String poetry, String poetryId) {
        this.poetry = poetry;
        this.poetryId = poetryId;
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
