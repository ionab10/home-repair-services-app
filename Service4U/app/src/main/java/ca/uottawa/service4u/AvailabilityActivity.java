package ca.uottawa.service4u;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class AvailabilityActivity extends AppCompatActivity {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
    private static final String dbTAG = "Database";

    protected FirebaseAuth mAuth;
    protected FirebaseDatabase database;
    protected DatabaseReference databaseUsers;

    int year;
    int month;
    int dayOfMonth;
    String dateString = null;

    ListView listTimeSlots;
    List<String> timeSlots;

    List<Long> availability;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_availability);

        Intent myIntent = getIntent(); // gets the previously created intent
        year = myIntent.getIntExtra("year",1);
        month= myIntent.getIntExtra("month",0);
        dayOfMonth= myIntent.getIntExtra("dayOfMonth",1);

        dateString = toDateString(dayOfMonth, month, year);

        setTitle(dateString);

        timeSlots = Arrays.asList(getResources().getStringArray(R.array.time_slots));

        listTimeSlots = (ListView) findViewById(R.id.listTimeSlots);
        TimeSlotList timeSlotAdapter = new TimeSlotList(AvailabilityActivity.this, timeSlots, availability, dateString);
        listTimeSlots.setAdapter(timeSlotAdapter);


        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        databaseUsers = database.getReference("Users");

        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            //get userType
            databaseUsers.child(user.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.
                    ServiceProvider appUser = dataSnapshot.getValue(ServiceProvider.class);

                    if (appUser.availability != null) {
                        availability = appUser.availability;
                    } else {
                        availability = new ArrayList<Long>();
                    }
                    Log.d(dbTAG, "Availability: " + availability);

                    updateForm();

                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.e(dbTAG, "Failed to read value.", error.toException());
                }
            });

        }

        updateForm();



    }


    private void updateForm(){
        Log.d("Form", "Updating form");
        TimeSlotList timeSlotAdapter = new TimeSlotList(AvailabilityActivity.this, timeSlots, availability, dateString);
        listTimeSlots.setAdapter(timeSlotAdapter);

    }

    private String toDateString(int dayOfMonth, int month, int year){

        String str = "";

        if (dayOfMonth < 10){
            str = str + "0" + String.valueOf(dayOfMonth);
        } else {
            str = str + String.valueOf(dayOfMonth);
        }

        str = str + "-";

        if (month < 10){
            str = str + "0" + String.valueOf(month);
        } else {
            str = str + String.valueOf(month);
        }

        str = str + "-";

        str = str + String.valueOf(year);

        return str;
    }

    public void setAvailability(View view){

        long dt;

        for (int i = 0; i < listTimeSlots.getChildCount(); i++) {
            RelativeLayout ts = (RelativeLayout) listTimeSlots.getChildAt(i);
            CheckBox cb = (CheckBox) ts.getChildAt(1);

            String dtStr = String.format("%s %s", dateString, timeSlots.get(i));
            try{
                dt = dateFormat.parse(dtStr).getTime();

                if (cb.isChecked()){
                    if (!availability.contains(dt)) {
                        availability.add(dt);
                    }
                } else {
                    availability.remove(dt);
                }
            } catch (Exception e){
                Log.e("parser", e.getMessage());
            }


        }


        Log.v("dbTAG", "Setting availability: " + availability.toString());

        FirebaseUser user = mAuth.getCurrentUser();
        databaseUsers.child(user.getUid()).child("availability").setValue(availability);

        Intent intent = new Intent(getApplicationContext(), AvailabilityCalendar.class);
        startActivityForResult (intent,0);
    }
}
