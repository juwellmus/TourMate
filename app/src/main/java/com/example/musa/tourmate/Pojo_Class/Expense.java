package com.example.musa.tourmate.Pojo_Class;

/**
 * Created by Musa on 5/26/2018.
 */

public class Expense {

    private String uid;
    private String currentDate;
    private String currentTime;
    private String eventName;
    private String eventExpense;
    private String expenseComment;

    public Expense() {
    }

    public Expense(String uid, String currentDate, String currentTime, String eventName, String eventExpense, String expenseComment) {
        this.uid = uid;
        this.currentDate = currentDate;
        this.currentTime = currentTime;
        this.eventName = eventName;
        this.eventExpense = eventExpense;
        this.expenseComment = expenseComment;
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

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventExpense() {
        return eventExpense;
    }

    public void setEventExpense(String eventExpense) {
        this.eventExpense = eventExpense;
    }

    public String getExpenseComment() {
        return expenseComment;
    }

    public void setExpenseComment(String expenseComment) {
        this.expenseComment = expenseComment;
    }
}
