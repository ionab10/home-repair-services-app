package ca.uottawa.service4u;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AvailabilityCalendar extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference databaseUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_availability_calendar);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        databaseUsers = database.getReference("Users");

        CalendarView calendarView = (CalendarView) findViewById(R.id.availabilityCal);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                                            int dayOfMonth) {

                Intent intent = new Intent(getApplicationContext(), AvailabilityActivity.class);
                intent.putExtra("year", year);
                month = month+1;    //months start at 0 instead of 1 for some weird reason
                intent.putExtra("month", month);
                intent.putExtra("dayOfMonth", dayOfMonth);
                startActivityForResult (intent,0);
            }
        });

        TextView upcomingAvailabilityText = findViewById(R.id.upcomingAvailabilityText);

        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            //get userType
            databaseUsers.child(user.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.
                    ServiceProvider appUser = dataSnapshot.getValue(ServiceProvider.class);
                    List<TimeInterval> myAvailability;

                    if (appUser.availability != null) {
                        myAvailability = appUser.availability;
                    } else {
                        myAvailability = new ArrayList<TimeInterval>();
                    }

                    String upcomingAvailability = "Upcoming Availability: \n";
                    long todayTime = new java.util.Date().getTime();
                    long FiveDays = 432000000;
                    
                    for(int i =0; i< myAvailability.size(); i++){
                       if((myAvailability.get(i).start >= todayTime) && (myAvailability.get(i).start<= todayTime + FiveDays))  {
                           upcomingAvailability = upcomingAvailability + myAvailability.get(i).toString() + "\n" ;
                       }
                    }

                    upcomingAvailabilityText.setText(upcomingAvailability);

                }

                @Override
                public void onCancelled(DatabaseError error) {
                }
            });

        }
    }

    public void back(View view) {
        finish();
    }
}
