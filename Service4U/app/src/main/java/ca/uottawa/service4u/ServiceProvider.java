package ca.uottawa.service4u;

import android.icu.util.DateInterval;

import java.util.List;

public class ServiceProvider extends User {

    List<Service> services;
    List<DateInterval> availability;

    public ServiceProvider(String firstName, String lastName, String userType, String phoneNumber) {
        super(firstName, lastName, userType, phoneNumber, null);
    }



}
