package com.example.musa.tourmate.Pojo_Class;

/**
 * Created by Musa on 5/30/2018.
 */

public class Moments {
    private String uid;
    private String currentDate;
    private String currentTime;
    private String image;

    public Moments() {
    }

    public Moments(String uid, String currentDate, String currentTime, String image) {
        this.uid = uid;
        this.currentDate = currentDate;
        this.currentTime = currentTime;
        this.image = image;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
