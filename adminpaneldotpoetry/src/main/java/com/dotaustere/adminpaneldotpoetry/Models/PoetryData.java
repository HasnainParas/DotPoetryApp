package com.dotaustere.adminpaneldotpoetry.Models;

import android.widget.EditText;

public class PoetryData {

     String poetName,poetryId,poetry,date;

    public PoetryData() {
    }

    public PoetryData(String poetName, String poetryId, String poetry, String date) {
        this.poetName = poetName;
        this.poetryId = poetryId;
        this.poetry = poetry;
        this.date = date;
    }

    public String getPoetName() {
        return poetName;
    }

    public void setPoetName(String poetName) {
        this.poetName = poetName;
    }

    public String getPoetryId() {
        return poetryId;
    }

    public void setPoetryId(String poetryId) {
        this.poetryId = poetryId;
    }

    public String getPoetry() {
        return poetry;
    }

    public void setPoetry(String poetry) {
        this.poetry = poetry;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
