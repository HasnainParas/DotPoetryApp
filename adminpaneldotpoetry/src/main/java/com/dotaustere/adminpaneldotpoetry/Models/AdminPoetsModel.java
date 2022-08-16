package com.dotaustere.adminpaneldotpoetry.Models;

public class AdminPoetsModel {

    String poetName, ageStart, ageEnd, poetPic,uuID;

    public AdminPoetsModel() {
    }

    public AdminPoetsModel(String poetName, String ageStart, String ageEnd, String poetPic, String uuID) {
        this.poetName = poetName;
        this.ageStart = ageStart;
        this.ageEnd = ageEnd;
        this.poetPic = poetPic;
        this.uuID = uuID;
    }

    public String getPoetName() {
        return poetName;
    }

    public void setPoetName(String poetName) {
        this.poetName = poetName;
    }

    public String getAgeStart() {
        return ageStart;
    }

    public void setAgeStart(String ageStart) {
        this.ageStart = ageStart;
    }

    public String getAgeEnd() {
        return ageEnd;
    }

    public void setAgeEnd(String ageEnd) {
        this.ageEnd = ageEnd;
    }

    public String getPoetPic() {
        return poetPic;
    }

    public void setPoetPic(String poetPic) {
        this.poetPic = poetPic;
    }

    public String getUuID() {
        return uuID;
    }

    public void setUuID(String uuID) {
        this.uuID = uuID;
    }
}
