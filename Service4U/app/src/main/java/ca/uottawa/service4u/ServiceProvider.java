package ca.uottawa.service4u;

import android.icu.util.DateInterval;

import java.sql.Time;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class ServiceProvider extends User {

    List<Service> services;
    List<TimeInterval> availability;

    public ServiceProvider(String firstName,
                           String lastName,
                           String userType,
                           String phoneNumber,
                           String address,
                           List<Service> services,
                           List<TimeInterval> availability) {
        super(firstName, lastName, userType, phoneNumber, address);

        this.services = services;
        this.availability = availability;
    }

    public ServiceProvider(){
        super();
        this.services = null;
        this.availability = null;
    }


    public long available(List<TimeInterval> homeownerAvailability, double hours){

        for (TimeInterval homeownerTI : homeownerAvailability){
            for (TimeInterval serviceProviderTI : availability){
                TimeInterval inter = homeownerTI.intersection(serviceProviderTI);

                if (inter.length()/60/60/1000 >= hours){
                    return inter.start;
                }
            }
        }
        return -1;
    }


}
