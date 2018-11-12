package ca.uottawa.service4u;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookJobActivity extends AppCompatActivity {
    private static final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
    private static final SimpleDateFormat datetimeFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");

    Calendar calendar = Calendar.getInstance();

    private Spinner serviceSpinner;
    private Spinner urgencySpinner;

    FirebaseDatabase database;
    DatabaseReference databaseUsers;
    DatabaseReference databaseServices;
    List<ServiceProvider> serviceProviders;
    List<Service> allServices;
    List<String> serviceNames;
    ArrayAdapter serviceAdapter;
    Service currentService;
    String currentUrgency;
    double timeLength;

    String[] times;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_job);

        times = getResources().getStringArray(R.array.times_array);
        ((TextView)findViewById(R.id.morningText)).setText(String.format("Morning: %s - %s", times[0], times[1]));
        ((TextView)findViewById(R.id.afternoonText)).setText(String.format("Afternoon: %s - %s", times[1], times[2]));
        ((TextView)findViewById(R.id.eveningText)).setText(String.format("Evening: %s - %s", times[2], times[3]));

        database = FirebaseDatabase.getInstance();

        serviceProviders = new ArrayList<ServiceProvider>();
        databaseUsers = database.getReference("Users");
        databaseUsers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                serviceProviders.clear();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    ServiceProvider appUser = postSnapshot.getValue(ServiceProvider.class);
                    if (appUser != null && appUser.getuserType().equals("service provider")) {
                        serviceProviders.add((ServiceProvider) appUser);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                allServices.clear();
                serviceNames.clear();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    Service s = postSnapshot.getValue(Service.class);
                    allServices.add(s);

                }

                //sort services by type and by name
                Collections.sort(allServices, (Service s1, Service s2) ->{
                    return s1.toString().compareToIgnoreCase(s2.toString());
                });

                for (Service s : allServices){
                    serviceNames.add(s.toString());
                }

                //update
                serviceAdapter = new ArrayAdapter(BookJobActivity.this,android.R.layout.simple_spinner_item, serviceNames);
                serviceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                serviceSpinner.setAdapter(serviceAdapter);
                currentService = allServices.get(0);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        //urgency spinner
        urgencySpinner = findViewById(R.id.urgencySpinner);
        ArrayAdapter<CharSequence> urgencyAdapter = ArrayAdapter.createFromResource(this,
                R.array.urgency_array, android.R.layout.simple_spinner_item);
        // Create an ArrayAdapter using the string array and a default spinner layout
        urgencyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        urgencySpinner.setAdapter(urgencyAdapter);
        currentUrgency = urgencyAdapter.getItem(0).toString();
        urgencySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                Log.v("urgency", (String) parent.getItemAtPosition(position));
                currentUrgency = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.v("item", "none");
            }
        });


        //urgency spinner
        Spinner timeSpinner = findViewById(R.id.timeSpinner);
        List<String> timeList = new ArrayList<String>();
        for (int i=1; i < 5; i++){
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
            if (sp.services!=null && sp.services.contains(service)){
                providers.add(sp);
            }
        }

        return providers;
    }

    public List<TimeInterval> getTimeIntervalsForDate(Date date, Map<Integer,List<List<String>>> availability) {
        List<TimeInterval> timeIntervals = new ArrayList<TimeInterval>();
        long start;
        long end;
        String dtStr = dateFormat.format(date);

        calendar.setTime(date);
        List<List<String>> timeSlots = availability.get(calendar.get(Calendar.DAY_OF_WEEK));   //get availability for day of week

        if (timeSlots!=null) {
            for (List<String> ts : timeSlots) {
                try {
                    start = datetimeFormat.parse(String.format("%s %s", dtStr, ts.get(0))).getTime();
                    end = datetimeFormat.parse(String.format("%s %s", dtStr, ts.get(1))).getTime();
                    timeIntervals.add(new TimeInterval(start, end));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        Log.d("timeIntervals", timeIntervals.toString());
        return timeIntervals;
    }

    public ArrayList<PotentialJob> findProviders(Service service, Map<Integer,List<List<String>>> availability, double hours, String urgency){
        ArrayList<PotentialJob> options = new ArrayList<PotentialJob>();
        List<ServiceProvider> providers = getAssociatedProviders(service);

        Date date = new Date(); //defaults to today
        long start = -1;
        List<TimeInterval> timeIntervals;

        calendar.setTime(date);
        switch (urgency){
            case "24 hours":
                calendar.add(Calendar.DAY_OF_YEAR, 1);
                break;
            case "1 week":
                calendar.add(Calendar.WEEK_OF_YEAR, 1);
                break;
            case "2 weeks":
                calendar.add(Calendar.WEEK_OF_YEAR, 2);
                break;
            case "1 month":
                calendar.add(Calendar.MONTH, 1);
                break;
            case "3 months":
                calendar.add(Calendar.MONTH, 3);
                break;

        }
        Date lastDate = calendar.getTime();

        while (date.before(lastDate)) {

            timeIntervals = getTimeIntervalsForDate(date,availability);

            for (ServiceProvider p : providers) {
                start = p.available(timeIntervals,hours);
                Log.d("start", p.toString()+ " " + String.valueOf(start));
                if (start >= 0) {
                    PotentialJob option = new PotentialJob(start, (long) (start + hours*60*60*1000),
                            p.getfirstName(),
                            p.getlastName(),
                            p.getId(),
                            p.rating);
                    options.add(option);
                }
            }

            date = addDays(date, 1);
        }

        return options;
    }

    public Map<Integer,List<List<String>>> readAvailability(){

        Map<Integer,List<List<String>>> availability = new HashMap<Integer, List<List<String>>>();

        TableLayout availTable = findViewById(R.id.availTable);
        List<List<String>> availabilityForDay;
        List<String> timeTuple;
        TableRow row;
        CheckBox cb;

        for (int i = 0; i< availTable.getChildCount(); i++){
            row = (TableRow) availTable.getChildAt(i);

            availabilityForDay = new ArrayList<List<String>>();
            timeTuple = new ArrayList<String>(2);
            boolean a = false;

            for (int j = 1; j < 4; j++){
                cb = (CheckBox) row.getChildAt(j);
                if (cb.isChecked() && !a){
                    timeTuple.add(times[j-1]);
                    a = true;
                } else if (!cb.isChecked() && a){
                    timeTuple.add(times[j-1]);
                    a = false;
                    availabilityForDay.add(timeTuple);
                    timeTuple = new ArrayList<String>(2);
                }
            }

            if (a){
                timeTuple.add(times[3]);
                a = false;
                availabilityForDay.add(timeTuple);
                timeTuple = new ArrayList<String>(2);
            }

            availability.put(i+1,availabilityForDay);

        }

        Log.d("availability", availability.toString());
        return availability;
    }


    public double timeDiff(String t0, String t1){
        double td = 0;
        try {
            td = timeFormat.parse(t1).getTime() - timeFormat.parse(t0).getTime();
            td = td/60/60/1000;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return td;
    }

    public boolean validateAvailability(Map<Integer,List<List<String>>> availability, double timeLength){
        for (List<List<String>> availabilityForDay : availability.values()){
            for (List<String> timeTuple : availabilityForDay){
                if (timeDiff(timeTuple.get(0), timeTuple.get(1)) >= timeLength){
                    return true;
                }
            }
        }
        return false;
    }

    public void handleForm(View view){

        Map<Integer,List<List<String>>> availability = readAvailability();
        TextView availabilityErrorText = findViewById(R.id.availabilityErrorText);

        if (validateAvailability(availability,timeLength)) {
            availabilityErrorText.setVisibility(view.GONE);

            ArrayList<PotentialJob> options = findProviders(currentService, availability, timeLength, currentUrgency);

            Log.v("options", options.toString());

            Intent intent = new Intent(getApplicationContext(), JobOptionsActivity.class);
            intent.putExtra("service",currentService.toString());
            intent.putExtra("service_id",currentService.getId());
            intent.putExtra("service_rate", currentService.getRatePerHour());
            intent.putExtra("time_length", timeLength);
            intent.putParcelableArrayListExtra("options",options);
            startActivityForResult(intent, 0);
        } else {
            availabilityErrorText.setText(String.format("You must be available for at least %.1f hours", timeLength));
            availabilityErrorText.setVisibility(view.VISIBLE);
        }

    }
}
