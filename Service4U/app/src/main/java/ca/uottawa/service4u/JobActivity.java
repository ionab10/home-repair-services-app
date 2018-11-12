package ca.uottawa.service4u;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class JobActivity extends AppCompatActivity {

    private static final SimpleDateFormat datetimeFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");

    FirebaseDatabase database;
    DatabaseReference databaseJobs;
    DatabaseReference databaseUsers;

    String jobID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job);

        Intent myIntent = getIntent(); // gets the previously created intent
        jobID = myIntent.getStringExtra("jobID");

        database = FirebaseDatabase.getInstance();
        databaseUsers = database.getReference("Users");

        databaseJobs = database.getReference("Jobs");
        databaseJobs.child(jobID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Job job = dataSnapshot.getValue(Job.class);

                //TODO Make this prettier

                ((TextView) findViewById(R.id.serviceTitleText)).setText(job.title);
                ((TextView) findViewById(R.id.datetimeText)).setText(datetimeFormat.format(new Date(job.startTime)));
                double timeLength = (job.endTime - job.startTime)/1000/60/60;
                ((TextView) findViewById(R.id.timeLengthText)).setText(String.format("%.1f hours", timeLength));
                ((TextView) findViewById(R.id.priceText)).setText(String.format("$%.2f",job.totalPrice));


                databaseUsers.child(job.serviceProviderID).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        ServiceProvider sp = dataSnapshot.getValue(ServiceProvider.class);
                        ((TextView) findViewById(R.id.providerNameText)).setText(String.format("%s %s", sp.getfirstName(), sp.getlastName()));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                //rating
                RatingBar ratingBar = findViewById(R.id.jobRatingBar);
                Date endTime = new Date(job.endTime);
                Date now = new Date();
                if (now.after(endTime)) {
                    ratingBar.setVisibility(View.VISIBLE);
                } else {
                    ratingBar.setVisibility(View.GONE);
                }

                ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                    @Override
                    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                        DatabaseReference dR = databaseJobs.child(jobID).child("rating");
                        dR.setValue(rating);

                        //update service provider rating
                        updateServiceProviderRating(job.serviceProviderID);

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void cancelJob(View view) {
        DatabaseReference dR = databaseJobs.child(jobID);
        dR.removeValue();

        updateAvailability(jobID);

        Toast.makeText(getApplicationContext(), "Job cancelled", Toast.LENGTH_LONG).show();
        finish();
    }

    public void updateAvailability(String jobID){

        //todo cancel job
        // remove from provider_ID booked
        // add to provider_ID availability

    }

    public void updateServiceProviderRating(String serviceProviderID) {
        double rating;

        rating = 0; //todo: find all jobs for serviceProviderID and calculate average rating

        DatabaseReference dR = databaseUsers.child(serviceProviderID).child("rating");
        dR.setValue(rating);
    }

}
