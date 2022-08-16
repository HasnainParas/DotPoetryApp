package com.dotaustere.adminpaneldotpoetry.Models;

public class GetData {
    String poetName, uuID;


    public GetData() {
    }

    public GetData(String poetName, String uuID) {
        this.poetName = poetName;
        this.uuID = uuID;
    }

    public String getPoetName() {
        return poetName;
    }

    public void setPoetName(String poetName) {
        this.poetName = poetName;
    }

    public String getUuID() {
        return uuID;
    }

    public void setUuID(String uuID) {
        this.uuID = uuID;
    }
}
