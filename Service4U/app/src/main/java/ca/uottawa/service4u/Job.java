package ca.uottawa.service4u;

import java.util.Date;

public class Job {
    private String homewownerID;
    private String serviceProviderID;
    private int year;
    private int month;
    private int day;
    private Time startTime;
    private Time endTime;
    private int rating;
    private double totalPrice;
    private String serviceID;
    private String notes;

    public Job(String homewownerID, String serviceProviderID, int year, int month, int day, Time startTime, Time endTime, int rating, double totalPrice, String serviceID, String notes) {
        this.homewownerID = homewownerID;
        this.serviceProviderID = serviceProviderID;
        this.year = year;
        this.month = month;
        this.day = day;
        this.startTime = startTime;
        this.endTime = endTime;
        this.rating = rating;
        this.totalPrice = totalPrice;
        this.serviceID = serviceID;
        this.notes = notes;
    }

    public String getHomewownerID() {
        return homewownerID;
    }

    public void setHomewownerID(String homewownerID) {
        this.homewownerID = homewownerID;
    }

    public String getServiceProviderID() {
        return serviceProviderID;
    }

    public void setServiceProviderID(String serviceProviderID) {
        this.serviceProviderID = serviceProviderID;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public Time getStartTime() {
        return startTime;
    }

    public void setStartTime(Time startTime) {
        this.startTime = startTime;
    }

    public Time getEndTime() {
        return endTime;
    }

    public void setEndTime(Time endTime) {
        this.endTime = endTime;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getServiceID() {
        return serviceID;
    }

    public void setServiceID(String serviceID) {
        this.serviceID = serviceID;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
