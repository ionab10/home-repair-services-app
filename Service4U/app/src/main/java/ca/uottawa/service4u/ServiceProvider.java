package ca.uottawa.service4u;

import android.icu.util.DateInterval;

import java.util.Date;
import java.util.List;

public class ServiceProvider extends User {

    List<Service> services;
    List<Long> availability;

    public ServiceProvider(String firstName,
                           String lastName,
                           String userType,
                           String phoneNumber,
                           String address,
                           List<Service> services,
                           List<Long> availability) {
        super(firstName, lastName, userType, phoneNumber, address);

        this.services = services;
        this.availability = availability;
    }

    public ServiceProvider(){
        super();
        this.services = null;
        this.availability = null;
    }


}
