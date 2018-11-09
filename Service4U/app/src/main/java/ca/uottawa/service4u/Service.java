package ca.uottawa.service4u;

import java.util.ArrayList;
import java.util.List;

public class Service {
    public String id;
    public String name;
    public String type;
    public double ratePerHour;

    public Service(){
        this.id = "";
        this.name = "";
        this.type = "";
        this.ratePerHour = 0;
    }

    public Service(String id, String name, String type, double ratePerHour){
        this.id = id;
        this.name = name;
        this.type = type;
        this.ratePerHour = ratePerHour;
    }


    public List<ServiceProvider> getAssociatedProviders(){
        List<ServiceProvider> providers = new ArrayList<ServiceProvider>();

        //TODO

        return providers;
    }
}
