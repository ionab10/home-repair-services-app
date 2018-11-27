package ca.uottawa.service4u;

import android.icu.util.DateInterval;
import android.os.Parcelable;
import android.support.annotation.Keep;
import android.util.Log;

import java.sql.Time;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Keep
public class ServiceProvider extends User {

    public List<Service> services;
    public List<TimeInterval> availability;
    public List<TimeInterval> booked;
    public String companyName;
    public double rating;
    public String description;
    public boolean licensed;

    public ServiceProvider(String id,
                           String firstName,
                           String lastName,
                           String userType,
                           String companyName,
                           String phoneNumber,
                           String address,
                           List<Service> services,
                           List<TimeInterval> availability,
                           List<TimeInterval> booked,
                           double rating,
                           String description,
                           boolean licensed) {
        super(id, firstName, lastName, userType, phoneNumber, address);

        this.services = services;
        this.availability = availability;
        this.booked = booked;
        this.rating = rating;
        this.description = description;
        this.licensed = licensed;
        this.companyName = companyName;
    }

    public ServiceProvider(){
        super();
        this.services = null;
        this.availability = null;
        this.booked = null;
        this.rating = 0;
        this.description = "";
        this.licensed = false;
        this.companyName = "";

    }

    public List<Service> getServices() {
        return services;
    }

    public void setServices(List<Service> services) {
        this.services = services;
    }

    public List<TimeInterval> getAvailability() {
        return availability;
    }

    public void setAvailability(List<TimeInterval> availability) {
        this.availability = availability;
    }

    public List<TimeInterval> getBooked() {
        return booked;
    }

    public void setBooked(List<TimeInterval> booked) {
        this.booked = booked;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isLicensed() {
        return licensed;
    }

    public void setLicensed(boolean licensed) {
        this.licensed = licensed;
    }

    public long available(List<TimeInterval> homeownerAvailability, double hours){

        for (TimeInterval homeownerTI : homeownerAvailability){
            if (availability != null) {

                for (TimeInterval serviceProviderTI : availability) {
                    serviceProviderTI.start = Math.max(serviceProviderTI.start,new Date().getTime());
                    TimeInterval inter = homeownerTI.intersection(serviceProviderTI);

                    Log.d("interLength", homeownerTI.toString() + " " + serviceProviderTI.toString() + " " + String.valueOf((float) inter.length() / 60 / 60 / 1000));
                    if ((float) inter.length() / 60 / 60 / 1000 >= hours) {
                        return inter.start;
                    }
                }
            }
        }
        return -1;
    }


}
