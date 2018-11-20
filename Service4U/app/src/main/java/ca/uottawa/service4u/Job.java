package ca.uottawa.service4u;

import java.util.Date;

public class Job {
    String id;
    public String homewownerID;
    public String serviceProviderID;
    public long startTime;
    public long endTime;
    public int rating;
    public double totalPrice;
    public String serviceID;
    public String notes;
    public String title;


    public Job() {
        this.id = "";
        this.homewownerID = "";
        this.serviceProviderID = "";
        this.startTime = 0;
        this.endTime = 0;
        this.rating = 0;
        this.totalPrice = 0;
        this.serviceID = "";
        this.notes = "";
        this.title = "";
    }

    public Job(String id,String title, String homewownerID, String serviceProviderID, long startTime, long endTime, int rating, double totalPrice, String serviceID, String notes) {
        this.id = id;
        this.homewownerID = homewownerID;
        this.serviceProviderID = serviceProviderID;
        this.startTime = startTime;
        this.endTime = endTime;
        this.rating = rating;
        this.totalPrice = totalPrice;
        this.serviceID = serviceID;
        this.notes = notes;
        this.title = title;
    }

    public double getTotalPrice(){
        return totalPrice;
    }

}
