package ca.uottawa.service4u;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class JobActivity extends AppCompatActivity {

    private static final SimpleDateFormat datetimeFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");

    FirebaseDatabase database;
    DatabaseReference databaseJobs;
    DatabaseReference databaseUsers;

    String jobID;
    String userType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job);

        Intent myIntent = getIntent(); // gets the previously created intent
        jobID = myIntent.getStringExtra("jobID");
        userType = myIntent.getStringExtra("userType");

        database = FirebaseDatabase.getInstance();
        databaseUsers = database.getReference("Users");

        databaseJobs = database.getReference("Jobs");
        databaseJobs.child(jobID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Job job = dataSnapshot.getValue(Job.class);

                //TODO Make this prettier

                ((TextView) findViewById(R.id.serviceTitleText)).setText(job.title);
                ((TextView) findViewById(R.id.datetimeText)).setText(datetimeFormat.format(new Date(job.startTime)));
                double timeLength = ((double)(job.endTime - job.startTime))/1000/60/60;
                ((TextView) findViewById(R.id.timeLengthText)).setText(String.format("%.1f hours", timeLength));
                ((TextView) findViewById(R.id.priceText)).setText(String.format("$%.2f",job.totalPrice));

                TextView nameText = findViewById(R.id.nameText);
                TextView phoneText = findViewById(R.id.phoneText);
                TextView addressText = findViewById(R.id.addressText);
                RatingBar ratingBar = findViewById(R.id.jobRatingBar);
                LinearLayout commentLayout = findViewById(R.id.commentLayout);
                TextView commentText = findViewById(R.id.commentText);
                EditText commentField = findViewById(R.id.editComment);


                Date endTime = new Date(job.endTime);
                Date now = new Date();
                if (now.after(endTime)) {
                    ratingBar.setVisibility(View.VISIBLE);
                    commentLayout.setVisibility(View.VISIBLE);
                } else {
                    ratingBar.setVisibility(View.GONE);
                    commentLayout.setVisibility(View.GONE);
                }
                ratingBar.setRating((float) job.rating);

                databaseUsers.child(job.serviceProviderID).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        ServiceProvider sp = dataSnapshot.getValue(ServiceProvider.class);

                        if (userType.equals("homeowner")) {
                            nameText.setText(String.format("%s %s", sp.getfirstName(), sp.getlastName()));
                            phoneText.setText(String.format("%s", sp.getphoneNumber()));
                            addressText.setText(String.format("%s", sp.getAddress()));
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                databaseUsers.child(job.homewownerID).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        User appUser = dataSnapshot.getValue(User.class);

                        if (userType.equals("service provider")) {
                            nameText.setText(String.format("%s %s", appUser.getfirstName(), appUser.getlastName()));
                            phoneText.setText(String.format("%s", appUser.getphoneNumber()));
                            addressText.setText(String.format("%s", appUser.getAddress()));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                if (userType.equals("homeowner")) {
                    ratingBar.setIsIndicator(false);
                    ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                        @Override
                        public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                            DatabaseReference dR = databaseJobs.child(jobID).child("rating");
                            //dR.setValue(rating); TODO uncomment when stuff is working

                            //update service provider rating
                            updateServiceProviderRating(job.serviceProviderID);

                        }
                    });

                    commentText.setText("Comments:");

                    commentField.setVisibility(View.VISIBLE);
                    commentField.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            DatabaseReference dR = databaseJobs.child(jobID).child("notes");
                            dR.setValue(s);
                        }
                    });

                } else if (userType.equals("service provider")){
                    ratingBar.setIsIndicator(true);
                    commentText.setText(String.format("Comments: %s", job.notes));
                    commentField.setVisibility(View.GONE);


                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void cancelJob(View view) {

        databaseJobs.child(jobID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Job job = dataSnapshot.getValue(Job.class);
                updateAvailability(job.serviceProviderID, job.startTime, job.endTime);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        DatabaseReference dR = databaseJobs.child(jobID);
        dR.removeValue();


        Toast.makeText(getApplicationContext(), "Job cancelled", Toast.LENGTH_LONG).show();
        finish();
    }

    public void updateAvailability(String providerID, long startTime, long endTime){

        databaseUsers.child(providerID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ServiceProvider appUser = dataSnapshot.getValue(ServiceProvider.class);
                List<TimeInterval> myAvailability;
                List<TimeInterval> myBooked;

                if (appUser.booked != null) {
                    myBooked = appUser.booked;
                } else {
                    myBooked = new ArrayList<TimeInterval>();
                }

                // remove from provider_ID booked
                myBooked = new TimeInterval(startTime,endTime).difference(myBooked);
                databaseUsers.child(providerID).child("booked").setValue(myBooked);


                if (appUser.availability != null) {
                    myAvailability = appUser.availability;
                } else {
                    myAvailability = new ArrayList<TimeInterval>();
                }

                // add to provider_ID availability
                myAvailability = new TimeInterval(startTime,endTime).union(myAvailability);
                databaseUsers.child(providerID).child("availability").setValue(myAvailability);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void updateServiceProviderRating(String serviceProviderID) {
        double rating;

        rating = 0; //todo: find all jobs for serviceProviderID and calculate average rating

        DatabaseReference dR = databaseUsers.child(serviceProviderID).child("rating");
        //dR.setValue(rating);
    }

}
