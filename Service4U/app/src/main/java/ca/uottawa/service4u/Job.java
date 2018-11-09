package ca.uottawa.service4u;

import java.util.Date;

public class Job {
    public String homewownerID;
    public String serviceProviderID;
    public int year;
    public int month;
    public int day;
    public long startTime;
    public long endTime;
    public int rating;
    public double totalPrice;
    public String serviceID;
    public String notes;

    public Job(String homewownerID, String serviceProviderID, int year, int month, int day, long startTime, long endTime, int rating, double totalPrice, String serviceID, String notes) {
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

}
