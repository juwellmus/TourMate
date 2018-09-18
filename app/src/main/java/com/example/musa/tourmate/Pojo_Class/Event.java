package com.example.musa.tourmate.Pojo_Class;

/**
 * Created by Musa on 5/26/2018.
 */

public class Event {

    private String eid;
    private String currentDate;
    private String currentTime;
    private String eventName;
    private String startLocation;
    private String destination;
    private String departureDate;
    private String createDate;
    private String budget;
    public Event() {
    }

    public Event(String eid, String currentDate, String currentTime, String eventName, String startLocation, String destination, String departureDate, String createDate, String budget) {
        this.eid = eid;
        this.currentDate = currentDate;
        this.currentTime = currentTime;
        this.eventName = eventName;
        this.startLocation = startLocation;
        this.destination = destination;
        this.departureDate = departureDate;
        this.createDate = createDate;
        this.budget = budget;

    }


    public String getEid() {
        return eid;
    }

    public void setEid(String eid) {
        this.eid = eid;
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

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(String startLocation) {
        this.startLocation = startLocation;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(String departureDate) {
        this.departureDate = departureDate;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getBudget() {
        return budget;
    }

    public void setBudget(String budget) {
        this.budget = budget;
    }
}
