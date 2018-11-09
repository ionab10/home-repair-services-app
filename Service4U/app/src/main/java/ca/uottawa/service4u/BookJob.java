package ca.uottawa.service4u;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookJob extends AppCompatActivity {

    Calendar calendar = Calendar.getInstance();

    private Spinner serviceSpinner;
    private Spinner urgencySpinner;
    private Spinner timeSpinner;

    FirebaseDatabase database;
    DatabaseReference databaseUsers;
    DatabaseReference databaseServices;
    List<ServiceProvider> serviceProviders;
    List<Service> allServices;
    List<String> serviceNames;
    ArrayAdapter<CharSequence> serviceAdapter;
    Service currentService;
    int currentUrgency;
    double timeLength;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_job);

        database = FirebaseDatabase.getInstance();

        serviceProviders = new ArrayList<ServiceProvider>();
        databaseUsers = database.getReference("Users");
        databaseUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                serviceProviders.clear();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    User appUser = postSnapshot.getValue(User.class);
                    if (appUser.getuserType() == "service provider") {
                        serviceProviders.add((ServiceProvider) appUser);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        //Service Type spinner
        allServices = new ArrayList<Service>();
        serviceNames = new ArrayList<String>();
        serviceSpinner = findViewById(R.id.serviceSpinner);

        serviceAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item, serviceNames);
        serviceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        serviceSpinner.setAdapter(serviceAdapter);

        serviceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                Log.v("current service", serviceNames.get(position));
                currentService = allServices.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.v("item", "none");
            }
        });

        databaseServices = database.getReference("Services");
        databaseServices.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                allServices.clear();
                serviceNames.clear();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    Service s = postSnapshot.getValue(Service.class);
                    allServices.add(s);

                }

                //sort services by type and by name
                Collections.sort(allServices, (Service s1, Service s2) ->{
                    String str1 = String.format("%s - %s", s1.getType(), s1.getName());
                    String str2 = String.format("%s - %s", s2.getType(), s2.getName());
                    return str1.compareToIgnoreCase(str2);
                });

                for (Service s : allServices){
                    serviceNames.add(String.format("%s - %s", s.getType(), s.getName()));
                }

                //update
                serviceAdapter = new ArrayAdapter(BookJob.this,android.R.layout.simple_spinner_item, serviceNames);
                serviceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                serviceSpinner.setAdapter(serviceAdapter);
                currentService = allServices.get(0);
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        //urgency spinner
        urgencySpinner = findViewById(R.id.urgencySpinner);
        ArrayAdapter<CharSequence> urgencyAdapter = ArrayAdapter.createFromResource(this,
                R.array.urgency_array, android.R.layout.simple_spinner_item);
        // Create an ArrayAdapter using the string array and a default spinner layout
        urgencyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        urgencySpinner.setAdapter(urgencyAdapter);
        currentUrgency = 0;
        urgencySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                Log.v("urgency", (String) parent.getItemAtPosition(position));
                currentUrgency = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.v("item", "none");
            }
        });


        //urgency spinner
        timeSpinner = findViewById(R.id.timeSpinner);
        List<String> timeList = new ArrayList<String>();
        for (int i=0; i < 5; i++){
            timeList.add(String.valueOf(i*0.5));
        }
        ArrayAdapter<CharSequence> timeAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item, timeList);
        timeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timeSpinner.setAdapter(timeAdapter);
        timeLength = 0.5;
        timeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                Log.v("time length", (String) parent.getItemAtPosition(position));
                timeLength = (position+1)*0.5;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.v("item", "none");
            }
        });

    }

    public Date addDays(Date date, int n) {
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_YEAR, n);
        return calendar.getTime();
    }


    public List<ServiceProvider> getAssociatedProviders(Service service){
        List<ServiceProvider> providers = new ArrayList<ServiceProvider>();

        for (ServiceProvider sp : serviceProviders){
            if (sp.services.contains(service)){
                providers.add(sp);
            }
        }
        return providers;
    }

    public List<TimeInterval> getTimeIntervalsForDate(Date date, Map<Integer,List<List<String>>> availability) {
        List<TimeInterval> timeIntervals = new ArrayList<TimeInterval>();
        long start;
        long end;
        String dtStr;

        calendar.setTime(date);
        List<List<String>> timeSlots = availability.get(calendar.get(Calendar.DAY_OF_WEEK));   //get availability for day of week

        for (List<String> ts : timeSlots){

            calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(ts.get(0).split(":")[0]));
            calendar.set(Calendar.MINUTE, Integer.parseInt(ts.get(0).split(":")[1]));
            start = calendar.getTime().getTime();
            calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(ts.get(1).split(":")[0]));
            calendar.set(Calendar.MINUTE, Integer.parseInt(ts.get(1).split(":")[1]));
            end = calendar.getTime().getTime();

            timeIntervals.add(new TimeInterval(start, end));
        }

        return timeIntervals;
    }

    public List<PotentialJob> findProviders(Service service, Map<Integer,List<List<String>>> availability, double hours, int urgency){
        List<PotentialJob> options = new ArrayList<PotentialJob>();
        List<ServiceProvider> providers = getAssociatedProviders(service);

        Date date = new Date(); //defaults to today
        long start = -1;
        List<TimeInterval> timeIntervals;

        for (int i=0; i< urgency; i++) {

            timeIntervals = getTimeIntervalsForDate(date,availability);

            for (ServiceProvider p : providers) {
                start = p.available(timeIntervals,hours);
                if (start >= 0) {
                    PotentialJob option = new PotentialJob(start, (long) (start + hours*60*60*1000), p);
                    options.add(option);
                }
            }

            date = addDays(date, 1);
        }

        return options;
    }

    public void handleForm(View view){

        Map<Integer,List<List<String>>> availability = new HashMap<>();

        //TODO

        //List<PotentialJob> options = findProviders(currentService,availability,timeLength,currentUrgency);

        Intent intent = new Intent(getApplicationContext(), JobOptionsActivity.class);
        //intent.putExtra("options", options); //TODO
        startActivityForResult (intent,0);

    }
}
